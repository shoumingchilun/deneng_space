package com.chilun.deneng.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chilun.deneng.Response.BaseResponse;
import com.chilun.deneng.Response.ResultCode;
import com.chilun.deneng.pojo.TaskForce;
import com.chilun.deneng.pojo.User;
import com.chilun.deneng.service.ITaskForceService;
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
@RequestMapping("/taskForce")
public class TaskForceController {
    //增删改查
    @Autowired
    ITaskForceService service;

    @Autowired
    AuthUtil authUtil;

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
    public BaseResponse addTaskForce(@RequestBody TaskForce taskForce,
                                     @SessionAttribute(name = "user", required = false) User user,
                                     @CookieValue(name = "JWT", required = false) String jwt) {
        if (!authUtil.isManager(user, jwt)) return new BaseResponse("非管理员", ResultCode.UN_AUTHOR);
        boolean save = service.save(taskForce);
        if (save) return new BaseResponse(null, ResultCode.SUCCESS);
        return new BaseResponse(null, ResultCode.FAILURE);
    }

    @PutMapping
    public BaseResponse updateTaskForce(@RequestBody TaskForce taskForce,
                                        @SessionAttribute(name = "user", required = false) User user,
                                        @CookieValue(name = "JWT", required = false) String jwt) {
        TaskForce byId = service.getById(taskForce.getId());
        if(byId==null){
            return new BaseResponse("修改的任务组不存在", ResultCode.FAILURE);
        }
        if (!authUtil.isManager(user, jwt) && !authUtil.isWantedUser(user, jwt, byId.getLeaderId()))
            return new BaseResponse("非管理员或任务组负责人", ResultCode.UN_AUTHOR);
        boolean update = service.updateById(taskForce);
        if (update) return new BaseResponse(null, ResultCode.SUCCESS);
        return new BaseResponse(null, ResultCode.FAILURE);
    }

    @DeleteMapping
    public BaseResponse deleteTaskForce(@RequestParam int id,
                                        @SessionAttribute(name = "user", required = false) User user,
                                        @CookieValue(name = "JWT", required = false) String jwt) {
        if (!authUtil.isManager(user, jwt)) return new BaseResponse("非管理员", ResultCode.UN_AUTHOR);
        boolean remove = service.removeById(id);
        if (remove) return new BaseResponse(null, ResultCode.SUCCESS);
        else return new BaseResponse(null, ResultCode.FAILURE);
    }
}
