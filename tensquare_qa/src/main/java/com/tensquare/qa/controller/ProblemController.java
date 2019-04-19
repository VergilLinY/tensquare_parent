package com.tensquare.qa.controller;
import java.util.Map;

import entity.StatusCode;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.tensquare.qa.pojo.Problem;
import com.tensquare.qa.service.ProblemService;

import entity.PageResult;
import entity.Result;
/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/problem")
public class ProblemController {

	private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }


    /**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,1000,"查询成功",problemService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,1000,"查询成功",problemService.findById(id));
	}


	/**
	 * 分页查询全部数据
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(value="/{page}/{size}",method=RequestMethod.GET)
	public Result findPage(@PathVariable int page,@PathVariable int size){
		Page<Problem> pageList = problemService.findPage(page, size);
		return new Result(true,1000,"查询成功",new PageResult<Problem>(pageList.getTotalElements(), pageList.getContent() ) );
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
		Page<Problem> pageList = problemService.findSearch(searchMap, page, size);
		return  new Result(true,1000,"查询成功",  new PageResult<Problem>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
	 * 增加
	 * @param problem
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Problem problem  ){
		problemService.add(problem);
		return new Result(true,1000,"增加成功");
	}
	
	/**
	 * 修改
	 * @param problem
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Problem problem, @PathVariable String id ){
		problem.setId(id);
		problemService.update(problem);		
		return new Result(true,1000,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		problemService.deleteById(id);
		return new Result(true,1000,"删除成功");
	}

    /**
     * 获取最新
     * @param label 所属单元
     * @param page  分页页码
     * @param size  分页大小
     * @return result
     */
	@GetMapping("/newlist/{label}/{page}/{size}")
    public Result getLatestProblems(@PathVariable String label, @PathVariable Integer page, @PathVariable Integer size){
        return new Result(true, StatusCode.OK, "查询成功", problemService.getLatestProblems(label, page, size));
    }

    /**
     * 获取最热
     * @param label 所属单元
     * @param page  分页页码
     * @param size  分页大小
     * @return result
     */
    @GetMapping("/hotlist/{label}/{page}/{size}")
    public Result getHottesProblems(@PathVariable String label, @PathVariable Integer page, @PathVariable Integer size){
        return new Result(true, StatusCode.OK, "查询成功", problemService.getHottestProblems(label, page, size));
    }

    /**
     * 获取等待回答
     * @param label 所属单元
     * @param page  分页页码
     * @param size  分页大小
     * @return result
     */
    @GetMapping("/waitlist/{label}/{page}/{size}")
    public Result getWaitedProblems(@PathVariable String label, @PathVariable Integer page, @PathVariable Integer size){
        return new Result(true, StatusCode.OK, "查询成功", problemService.getWaittedProblems(label, page, size));
    }
	
}
