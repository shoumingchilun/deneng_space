package com.chilun.deneng.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chilun.deneng.Response.BaseResponse;
import com.chilun.deneng.Response.ResultCode;
import com.chilun.deneng.pojo.Task;
import com.chilun.deneng.pojo.TaskPool;
import com.chilun.deneng.pojo.TaskUser;
import com.chilun.deneng.service.ITaskPoolService;
import com.chilun.deneng.service.ITaskUserService;
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
@RequestMapping("/taskUser")
public class TaskUserController {
    @Autowired
    ITaskUserService service;

    @GetMapping("/userId")
    public BaseResponse queryPoolIdsByUserId(@RequestParam int id) {
        QueryWrapper<TaskUser> qw = new QueryWrapper<>();
        qw.eq("user_id", id);
        qw.select("pool_id");
        List<Object> list = service.getBaseMapper().selectObjs(qw);
        return new BaseResponse(JSON.toJSONString(list), ResultCode.SUCCESS);
    }

    @GetMapping("/poolId")
    public BaseResponse queryUserIdsByPoolId(@RequestParam int id) {
        QueryWrapper<TaskUser> qw = new QueryWrapper<>();
        qw.eq("pool_id", id);
        qw.select("user_id");
        List<Object> list = service.getBaseMapper().selectObjs(qw);
        return new BaseResponse(JSON.toJSONString(list), ResultCode.SUCCESS);
    }

    @PostMapping
    public BaseResponse addTaskUser(@RequestBody TaskUser taskUser) {
        boolean save = service.save(taskUser);
        if (save) {
            return new BaseResponse(null, ResultCode.SUCCESS);
        }
        return new BaseResponse(null, ResultCode.FAILURE);
    }

    @DeleteMapping
    public BaseResponse deleteTaskUser(@RequestBody TaskUser taskUser) {
        QueryWrapper<TaskUser> qw = new QueryWrapper<>();
        qw.eq("user_id", taskUser.getUserId());
        qw.eq("pool_id", taskUser.getPoolId());
        int delete = service.getBaseMapper().delete(qw);
        return new BaseResponse(JSON.toJSONString(delete), ResultCode.SUCCESS);
    }
}
