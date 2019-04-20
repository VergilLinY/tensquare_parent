package com.tensquare.user.service;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import utils.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 服务层
 *
 * @author Administrator
 */
@Service
@Transactional
public class UserService {

    private final UserDao userDao;
    private final IdWorker idWorker;
    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate redisTemplate;

    public UserService(UserDao userDao, IdWorker idWorker, RabbitTemplate rabbitTemplate, RedisTemplate redisTemplate, BCryptPasswordEncoder encoder) {
        this.userDao = userDao;
        this.idWorker = idWorker;
        this.rabbitTemplate = rabbitTemplate;
        this.redisTemplate = redisTemplate;
        this.encoder = encoder;
    }

    /**
     * 发送手机验证码
     */
    public void sendSms(String mobile) {
        //1 生成手机验证码
        int randomNum = new Random().nextInt(999999);
        if (randomNum < 100000) {
            randomNum += 100000;
        }
        System.out.println("验证码是:" + randomNum);
        //2 将验证码作为消息发送到RabbitMQ
        Map<String, String> map = new HashMap();
        map.put("phoneNumber", mobile);
        map.put("checkCode", randomNum + "");
        rabbitTemplate.convertAndSend("sms", map);
        //3 将验证码存入redis
        redisTemplate.opsForValue().set("phoneNumber_" + mobile, randomNum + "", 5, TimeUnit.MINUTES);
    }

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<User> findAll() {
        return userDao.findAll();
    }

    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @return
     */
    public Page<User> findPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return userDao.findAll(pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<User> findSearch(Map whereMap, int page, int size) {
        Specification<User> specification = where(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return userDao.findAll(specification, pageRequest);
    }


    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public User findById(String id) {
        return userDao.findById(id).get();
    }

    /**
     * 增加
     *
     * @param user
     */
    public void add(User user) {
        user.setId(idWorker.nextId() + "");
        userDao.save(user);
    }

    /**
     * 修改
     *
     * @param user
     */
    public void update(User user) {
        userDao.save(user);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        userDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<User> where(Map searchMap) {

        return new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 手机号码
                if (searchMap.get("mobile") != null && !"".equals(searchMap.get("mobile"))) {
                    predicateList.add(cb.like(root.get("mobile").as(String.class), "%" + (String) searchMap.get("mobile") + "%"));
                }
                // 密码
                if (searchMap.get("password") != null && !"".equals(searchMap.get("password"))) {
                    predicateList.add(cb.like(root.get("password").as(String.class), "%" + (String) searchMap.get("password") + "%"));
                }
                // 昵称
                if (searchMap.get("nickname") != null && !"".equals(searchMap.get("nickname"))) {
                    predicateList.add(cb.like(root.get("nickname").as(String.class), "%" + (String) searchMap.get("nickname") + "%"));
                }
                // 性别
                if (searchMap.get("sex") != null && !"".equals(searchMap.get("sex"))) {
                    predicateList.add(cb.like(root.get("sex").as(String.class), "%" + (String) searchMap.get("sex") + "%"));
                }
                // 头像
                if (searchMap.get("avatar") != null && !"".equals(searchMap.get("avatar"))) {
                    predicateList.add(cb.like(root.get("avatar").as(String.class), "%" + (String) searchMap.get("avatar") + "%"));
                }
                // E-Mail
                if (searchMap.get("email") != null && !"".equals(searchMap.get("email"))) {
                    predicateList.add(cb.like(root.get("email").as(String.class), "%" + (String) searchMap.get("email") + "%"));
                }
                // 兴趣
                if (searchMap.get("interest") != null && !"".equals(searchMap.get("interest"))) {
                    predicateList.add(cb.like(root.get("interest").as(String.class), "%" + (String) searchMap.get("interest") + "%"));
                }
                // 个性
                if (searchMap.get("personality") != null && !"".equals(searchMap.get("personality"))) {
                    predicateList.add(cb.like(root.get("personality").as(String.class), "%" + (String) searchMap.get("personality") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    private final BCryptPasswordEncoder encoder;

    //注册用户
    public void register(String code, User user) {
        //从redis中取出正确的验证码,校验
        String redisCheckCode = (String) redisTemplate.opsForValue().get("phoneNumber_" + user.getMobile());
        //错误 => 跑出异常提示验证码错误
        if (StringUtils.isEmpty(redisCheckCode)) {
            throw new RuntimeException("验证码超时");
        }
        if (!redisCheckCode.equals(code)) {
            throw new RuntimeException("验证码错误");
        }
        //正确 => 调用Dao保存user
        user.setFanscount(0); //粉丝数
        user.setFollowcount(0); //关注数
        user.setId(idWorker.nextId() + "");
        user.setRegdate(new Date());//注册时间
        user.setOnline(0L);//在线时间

        //移除redis中的验证码
        redisTemplate.delete("phoneNumber_" + user.getMobile());
        //使用BCript加密
        user.setPassword(encoder.encode(user.getPassword()));
        //保存用户
        userDao.save(user);
    }

    //登录方法
    public User login(User user) {

        //1 使用手机号从数据库中查询user
        User loginUser = userDao.findByMobile(user.getMobile());
        //查询不到=>手机号不存在 => 返回null
        if (loginUser == null) {
            return null;
        }
        //2 使用工具类比对密码是否一致 -> (参数1:明文密码 参数2:密文密码)
        if (!encoder.matches(user.getPassword(), loginUser.getPassword())) {
            //不一致 => 密码错误=> 返回null
            return null;
        }
        //3 返回查询到的user
        return loginUser;
    }


    public void changeFansCount(String userid, Integer count) {
        userDao.changeFansCount(userid, count);
    }

    public void changeFollowCount(String userid, Integer count) {
        userDao.changeFollowCount(userid, count);
    }
}
