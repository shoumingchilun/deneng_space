package com.chilun.deneng.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chilun.deneng.Response.BaseResponse;
import com.chilun.deneng.Response.ResultCode;
import com.chilun.deneng.pojo.Article;
import com.chilun.deneng.pojo.Task;
import com.chilun.deneng.pojo.User;
import com.chilun.deneng.service.IArticleService;
import com.chilun.deneng.service.ITaskService;
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
@RequestMapping("/task")
public class TaskController {
    //增删改查
    @Autowired
    ITaskService service;
    @Autowired
    AuthUtil authUtil;

    @GetMapping
    public BaseResponse queryTaskById(@RequestParam int id) {
        Task task = service.getById(id);
        if (task == null) {
            return new BaseResponse("记录不存在", ResultCode.FAILURE);
        }
        return new BaseResponse(JSON.toJSONString(task), ResultCode.SUCCESS);
    }

    @GetMapping("/poolId")
    public BaseResponse queryTaskByPoolID(@RequestParam int poolId) {
        QueryWrapper<Task> qw = new QueryWrapper<>();
        qw.eq("pool_id", poolId);
        List<Task> tasks = service.getBaseMapper().selectList(qw);
        return new BaseResponse(JSON.toJSONString(tasks), ResultCode.SUCCESS);
    }

    @GetMapping("/userId")
    public BaseResponse queryTaskByUserID(@RequestParam int userId) {
        QueryWrapper<Task> qw = new QueryWrapper<>();
        qw.eq("label_user_id", userId).or()
                .eq("quality_user_id", userId).or()
                .eq("final_user_id", userId)
                .orderByAsc("last_changed_time");
        List<Task> tasks = service.getBaseMapper().selectList(qw);
        return new BaseResponse(JSON.toJSONString(tasks), ResultCode.SUCCESS);
    }

    @PostMapping
    public BaseResponse addTask(@RequestBody Task task,
                                @SessionAttribute(name = "user", required = false) User user,
                                @CookieValue(name = "JWT", required = false) String jwt) {
        if (!authUtil.isManager(user, jwt)) return new BaseResponse("非管理员", ResultCode.UN_AUTHOR);
        boolean save = service.save(task);
        if (save) return new BaseResponse("添加成功", ResultCode.SUCCESS);
        return new BaseResponse("添加失败", ResultCode.FAILURE);
    }

    @PutMapping
    public BaseResponse updateTaskWithoutState(@RequestBody Task task) {
        boolean update = service.updateById(task);
        if (update) return new BaseResponse("修改成功", ResultCode.SUCCESS);
        return new BaseResponse("修改失败", ResultCode.FAILURE);
    }

    //如果存在状态变更，则调用此方法
    @PutMapping("/withState")
    public BaseResponse updateTaskWithState(@RequestBody Task task) {
        boolean update = service.updateByIdWithState(task);
        if (update) return new BaseResponse("修改成功", ResultCode.SUCCESS);
        return new BaseResponse("修改失败", ResultCode.FAILURE);
    }

    @DeleteMapping
    public BaseResponse deleteTask(@RequestParam int id,
                                   @SessionAttribute(name = "user", required = false) User user,
                                   @CookieValue(name = "JWT", required = false) String jwt) {
        if (!authUtil.isManager(user, jwt)) return new BaseResponse("非管理员", ResultCode.UN_AUTHOR);
        boolean remove = service.removeById(id);
        if (remove) return new BaseResponse("删除成功", ResultCode.SUCCESS);
        else return new BaseResponse("删除失败", ResultCode.FAILURE);
    }
}
