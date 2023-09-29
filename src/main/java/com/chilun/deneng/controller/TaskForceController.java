package com.chilun.deneng.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chilun.deneng.Response.BaseResponse;
import com.chilun.deneng.Response.ResultCode;
import com.chilun.deneng.pojo.Article;
import com.chilun.deneng.pojo.TaskForce;
import com.chilun.deneng.service.IArticleService;
import com.chilun.deneng.service.ITaskForceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author chilun
 * @since 2023-09-28
 */
@RestController
@RequestMapping("/taskForce")
public class TaskForceController {
    //增删改查
    @Autowired
    ITaskForceService service;

    @GetMapping
    public BaseResponse queryTaskForceById(@RequestParam int id) {
        TaskForce taskForce = service.getById(id);
        if (taskForce == null) {
            return new BaseResponse("任务组不存在", ResultCode.FAILURE);
        }
        return new BaseResponse(JSON.toJSONString(taskForce), ResultCode.SUCCESS);
    }

    @GetMapping("/all")
    public BaseResponse queryTaskForceAll() {
        List<TaskForce> list = service.list();
        return new BaseResponse(JSON.toJSONString(list), ResultCode.SUCCESS);
    }

    @GetMapping("/{name}")
    public BaseResponse queryTaskForceByName(@PathVariable String name) {
        QueryWrapper<TaskForce> qw = new QueryWrapper<>();
        qw.eq("name", name);
        List<TaskForce> taskForce = service.getBaseMapper().selectList(qw);
        return taskForce.size() == 0 ?
                new BaseResponse(null, ResultCode.SUCCESS) :
                new BaseResponse(JSON.toJSONString(taskForce.get(0)), ResultCode.SUCCESS);
    }

    @PostMapping
    public BaseResponse addTaskForce(@RequestBody TaskForce taskForce) {
        boolean save = service.save(taskForce);
        if (save) return new BaseResponse(null, ResultCode.SUCCESS);
        return new BaseResponse(null, ResultCode.FAILURE);
    }

    @PutMapping
    public BaseResponse updateTaskForce(@RequestBody TaskForce taskForce) {
        boolean update = service.updateById(taskForce);
        if (update) return new BaseResponse(null, ResultCode.SUCCESS);
        return new BaseResponse(null, ResultCode.FAILURE);
    }

    @DeleteMapping
    public BaseResponse deleteTaskForce(@RequestParam int id) {
        boolean remove = service.removeById(id);
        if (remove) return new BaseResponse(null, ResultCode.SUCCESS);
        else return new BaseResponse(null, ResultCode.FAILURE);
    }
}
