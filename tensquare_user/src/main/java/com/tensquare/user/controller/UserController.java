package com.tensquare.user.controller;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private JwtUtil jwtUtil;

   /**
    * 注册用户
    * @return
    */
   @RequestMapping(value="/register/{code}",method= RequestMethod.POST)
   public Result register(@PathVariable String code,@RequestBody User user){
      //调用service完成注册逻辑
      userService.register(code,user);
      return new Result(true, StatusCode.OK,"注册成功");
   } /**
    * 登录用户
    * @return
    */
   @RequestMapping(value="/login",method= RequestMethod.POST)
   public Result login(@RequestBody User user){ //将用户名和密码接收到user对象
      //调用service校验用户名密码
      User loginUser = userService.login(user);

      if(loginUser == null){// 登录失败
							return new Result(false, StatusCode.LOGINERROR,"登录失败");
						}

				//登录成功 => 生成token
				String token = jwtUtil.generateToken(loginUser.getId(), loginUser.getNickname(), "user");

				Map map = new HashMap();
				map.put("token",token);
				map.put("name",loginUser.getNickname());
				map.put("avatar",loginUser.getAvatar()); //头像


      return new Result(true, StatusCode.OK,"登录成功",map);
   }

	/**
		* 发送短信
		* @return
		*/
	@RequestMapping(value="/sendsms/{mobile}",method= RequestMethod.POST)
	public Result sendSms(@PathVariable("mobile") String mobile){
		userService.sendSms(mobile);
		return new Result(true, StatusCode.OK,"发送成功");
	}


	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,1000,"查询成功",userService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,1000,"查询成功",userService.findById(id));
	}


	/**
	 * 分页查询全部数据
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(value="/{page}/{size}",method=RequestMethod.GET)
	public Result findPage(@PathVariable int page,@PathVariable int size){
		Page<User> pageList = userService.findPage(page, size);
		return new Result(true,1000,"查询成功",new PageResult<User>(pageList.getTotalElements(), pageList.getContent() ) );
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,1000,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
	 * 增加
	 * @param user
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody User user  ){
		userService.add(user);
		return new Result(true,1000,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.update(user);		
		return new Result(true,100,"修改成功");
	}



	/**
		* 登录后,需要与浏览器约定,每次请求服务器都应在请求头中携带token.用于在服务端进行校验
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
	/* //1 . 获得 Authorization 头内容(token)
	String header =	request.getHeader("Authorization");
					//获得不到 => 抛出异常提示
		if(StringUtils.isEmpty(header)){
				throw new RuntimeException("请先登录");
		}
		//2 token是否以"Bearer "开头
		if(!header.startsWith("Bearer ")){
					//不是 => 抛出异常提示
			throw new RuntimeException("别黑我");
		}
		//3 截掉"Bearer "部分,并解析token
		String token =  header.substring(7);
		Claims claims = jwtUtil.parseToken(token);
		if(claims == null){
		//解析失败 => 抛出异常提示
			throw new RuntimeException("登录过期");
		}
		//4 判断角色是否为"admin"
		String role = (String) claims.get("role");
		if(!"admin".equals(role)){
		//不是admin => 抛出异常提示
			throw new RuntimeException("没有权限");
		}*/
	//判断当前用户是否有删除权限
		Object claims_admin = request.getAttribute("claims_admin");

		if(claims_admin==null){
			throw new RuntimeException("没有权限");
		}
		//5 调用service删除
		userService.deleteById(id);
		return new Result(true,1000,"删除成功");
	}

	/**
		* 修改粉丝数量
		* @return
		*/
	@RequestMapping(value="/fanscount/{userid}/{count}",method= RequestMethod.PUT)
	public Result changeFansCount(@PathVariable String userid,@PathVariable Integer count){
		//修改粉丝数量
		userService.changeFansCount(userid,count);
		return new Result(true, StatusCode.OK,"修改成功");
	}
	/**
		* 修改关注数量
		* @return
		*/
	@RequestMapping(value="/followcount/{userid}/{count}",method= RequestMethod.PUT)
	public Result changeFollowCount(@PathVariable String userid,@PathVariable Integer count){
		//修改粉丝数量
		userService.changeFollowCount(userid,count);
		return new Result(true, StatusCode.OK,"修改成功");
	}
}
