package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/label")
@CrossOrigin // 开启跨域访问
public class LabelController {

    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    // 增
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Label label){
        labelService.save(label);
        return new Result(StatusCode.OK,true,"添加成功");
    }
    // 删
    @RequestMapping(value = "/{labelId}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable String labelId){
        labelService.delete(labelId);
        return new Result(StatusCode.OK, true, "删除成功");
    }
    // 改
    @RequestMapping(value = "/{labelId}",method = RequestMethod.PUT)
    public Result update(@PathVariable String labelId, @RequestBody Label label){
        labelService.update(labelId, label);
        return new Result(StatusCode.OK, true, "修改成功");
    }

    // 根据ID查
    @RequestMapping(value = "/{labelId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String labelId){
        Label label = labelService.findById(labelId);
        return new Result(StatusCode.OK, true, "查询成功", label);
    }

    // 查所有
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        List<Label> labels = labelService.findAll();
        return new Result(StatusCode.OK, true, "查询成功", labels);
    }

    // 根据条件查
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findByCondition(@RequestBody Label label){
        List<Label> labels = labelService.findByCondition(label);
        return new Result(StatusCode.OK, true, "查询成功", labels);
    }

    // 根据条件分页查
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findPageByCondition(@RequestBody Label label,@PathVariable Integer page, @PathVariable Integer size){
        PageResult<Label> pageResult = labelService.findPageByCondition(page, size, label);
        return new Result(StatusCode.OK, true, "查询成功", pageResult);
    }
}
