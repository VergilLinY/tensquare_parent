package com.tensquare.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.user.pojo.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface UserDao extends JpaRepository<User,String>,JpaSpecificationExecutor<User>{

   User findByMobile(String mobile);
   @Modifying
   @Query(" update User set fanscount = fanscount + ?2 where id = ?1 ")
   void changeFansCount(String userid, Integer count);
   @Modifying
   @Query(" update User set followcount = followcount + ?2 where id = ?1 ")
   void changeFollowCount(String userid, Integer count);
}
