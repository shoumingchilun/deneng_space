package com.chilun.deneng.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chilun.deneng.Response.BaseResponse;
import com.chilun.deneng.Response.ResultCode;
import com.chilun.deneng.pojo.TaskForce;
import com.chilun.deneng.pojo.TaskPool;
import com.chilun.deneng.pojo.User;
import com.chilun.deneng.service.ITaskForceService;
import com.chilun.deneng.service.ITaskPoolService;
import com.chilun.deneng.tools.auth.AuthUtil;
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
@RequestMapping("/taskPool")
public class TaskPoolController {
    //增删改查
    @Autowired
    ITaskPoolService service;

    @Autowired
    AuthUtil authUtil;

    @GetMapping
    public BaseResponse queryTaskPoolById(@RequestParam int id) {
        TaskPool taskPool = service.getById(id);
        if (taskPool == null) {
            return new BaseResponse("任务池不存在", ResultCode.FAILURE);
        }
        return new BaseResponse(JSON.toJSONString(taskPool), ResultCode.SUCCESS);
    }

    @GetMapping("/all")
    public BaseResponse queryTaskPoolAll() {
        List<TaskPool> list = service.list();
        return new BaseResponse(JSON.toJSONString(list), ResultCode.SUCCESS);
    }

    @GetMapping("/{name}")
    public BaseResponse queryTaskPoolByName(@PathVariable String name) {
        QueryWrapper<TaskPool> qw = new QueryWrapper<>();
        qw.eq("name", name);
        List<TaskPool> taskPool = service.getBaseMapper().selectList(qw);
        return taskPool.size() == 0 ?
                new BaseResponse(null, ResultCode.SUCCESS) :
                new BaseResponse(JSON.toJSONString(taskPool.get(0)), ResultCode.SUCCESS);
    }

    @GetMapping("/taskForce")
    public BaseResponse queryTaskPoolByTaskForceId(@RequestParam int taskForceId) {
        QueryWrapper<TaskPool> qw = new QueryWrapper<>();
        qw.eq("task_force_id", taskForceId);
        List<TaskPool> taskPool = service.getBaseMapper().selectList(qw);
        return new BaseResponse(JSON.toJSONString(taskPool), ResultCode.SUCCESS);
    }

    @PostMapping
    public BaseResponse addTaskPool(@RequestBody TaskPool taskPool,
                                    @SessionAttribute(name = "user", required = false) User user,
                                    @CookieValue(name = "JWT", required = false) String jwt) {
        if (!authUtil.isManager(user, jwt)) return new BaseResponse("非管理员", ResultCode.UN_AUTHOR);
        boolean save = service.save(taskPool);
        if (save) return new BaseResponse(null, ResultCode.SUCCESS);
        return new BaseResponse(null, ResultCode.FAILURE);
    }

    @PutMapping
    public BaseResponse updateTaskPool(@RequestBody TaskPool taskPool,
                                       @SessionAttribute(name = "user", required = false) User user,
                                       @CookieValue(name = "JWT", required = false) String jwt) {
        if (!authUtil.isManager(user, jwt)) return new BaseResponse("非管理员", ResultCode.UN_AUTHOR);
        boolean update = service.updateById(taskPool);
        if (update) return new BaseResponse(null, ResultCode.SUCCESS);
        return new BaseResponse(null, ResultCode.FAILURE);
    }

    @DeleteMapping
    public BaseResponse deleteTaskPool(@RequestParam int id,
                                       @SessionAttribute(name = "user", required = false) User user,
                                       @CookieValue(name = "JWT", required = false) String jwt) {
        if (!authUtil.isManager(user, jwt)) return new BaseResponse("非管理员", ResultCode.UN_AUTHOR);
        boolean remove = service.removeById(id);
        if (remove) return new BaseResponse(null, ResultCode.SUCCESS);
        else return new BaseResponse(null, ResultCode.FAILURE);
    }
}
