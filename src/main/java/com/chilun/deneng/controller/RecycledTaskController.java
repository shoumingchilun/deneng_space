package com.chilun.deneng.controller;

import com.chilun.deneng.pojo.Task;
import com.chilun.deneng.service.IRecycledTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chilun
 * @since 2023-09-28
 */
@RestController
@RequestMapping("/recycledTask")
public class RecycledTaskController {
    @Autowired
    IRecycledTaskService service;

    public void addTask(Task task) {
        System.out.println(task.getId()+" add to Recycled Table");
    }

    public void removeTask(Task task) {
        System.out.println(task.getId()+" remove from Recycled Table");
    }
}
