package com.tensquare.qa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import com.tensquare.qa.dao.ReplyDao;
import com.tensquare.qa.pojo.Reply;
import utils.IdWorker;

/**
 * 服务层
 *
 * @author Administrator
 */
@Service
public class ReplyService {

    private final ReplyDao replyDao;

    private final IdWorker idWorker;

    public ReplyService(ReplyDao replyDao, IdWorker idWorker) {
        this.replyDao = replyDao;
        this.idWorker = idWorker;
    }

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<Reply> findAll() {
        return replyDao.findAll();
    }

    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @return
     */
    public Page<Reply> findPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return replyDao.findAll(pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<Reply> findSearch(Map whereMap, int page, int size) {
        Specification<Reply> specification = where(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return replyDao.findAll(specification, pageRequest);
    }


    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public Reply findById(String id) {
        return replyDao.findById(id).get();
    }

    /**
     * 增加
     *
     * @param reply
     */
    public void add(Reply reply) {
        reply.setId(idWorker.nextId() + "");
        replyDao.save(reply);
    }

    /**
     * 修改
     *
     * @param reply
     */
    public void update(Reply reply) {
        replyDao.save(reply);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        replyDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<Reply> where(Map searchMap) {

        return (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<Predicate>();
            // 编号
            if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
            }
            // 问题ID
            if (searchMap.get("problemid") != null && !"".equals(searchMap.get("problemid"))) {
                predicateList.add(cb.like(root.get("problemid").as(String.class), "%" + (String) searchMap.get("problemid") + "%"));
            }
            // 回答内容
            if (searchMap.get("content") != null && !"".equals(searchMap.get("content"))) {
                predicateList.add(cb.like(root.get("content").as(String.class), "%" + (String) searchMap.get("content") + "%"));
            }
            // 回答人ID
            if (searchMap.get("userid") != null && !"".equals(searchMap.get("userid"))) {
                predicateList.add(cb.like(root.get("userid").as(String.class), "%" + (String) searchMap.get("userid") + "%"));
            }
            // 回答人昵称
            if (searchMap.get("nickname") != null && !"".equals(searchMap.get("nickname"))) {
                predicateList.add(cb.like(root.get("nickname").as(String.class), "%" + (String) searchMap.get("nickname") + "%"));
            }

            return cb.and(predicateList.toArray(new Predicate[0]));

        };

    }

}
