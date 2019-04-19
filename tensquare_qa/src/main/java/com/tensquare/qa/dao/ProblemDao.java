package com.tensquare.qa.dao;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{

	//最新问题
    @Query(value = "select * from tb_problem where id in (select problemid from tb_pl where labelid = 1) order by replytime DESC", nativeQuery = true)
    public List<Problem> getLatestPageList(String labelid, PageRequest pageRequest);

	//最热门问题
    @Query(value = "select * from tb_problem where id in (select problemid from tb_pl where labelid = 1) order by reply DESC", nativeQuery = true)
    public List<Problem> getHottestPageList(String labelid, PageRequest pageRequest);

    //等待回答的问题
    @Query(value = "select * from tb_problem where id in (select problemid from tb_pl where labelid = 1) AND reply = 0 order by createtime DESC", nativeQuery = true)
    public List<Problem> getWaitedPageList(String labelid, PageRequest pageRequest);

}
