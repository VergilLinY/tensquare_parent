package com.tensquare.article.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.article.pojo.Article;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
//JPQL | HQL
public interface ArticleDao extends JpaRepository<Article,String>,JpaSpecificationExecutor<Article>{

   //审核文章
   @Modifying //表示需要修改数据库
   @Query("update Article set state = '1' where id = ?1")
   void examine(String articleId);
   //点赞文章
   @Modifying //表示需要修改数据库
   @Query("update Article set thumbup = thumbup+1 where id = ?1")
   void thumbup(String articleId);

}
