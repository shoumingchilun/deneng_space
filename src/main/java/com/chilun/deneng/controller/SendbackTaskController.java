package com.chilun.deneng.controller;

import com.chilun.deneng.pojo.Task;
import com.chilun.deneng.service.ISendbackTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  暂未完成
 * </p>
 *
 * @author chilun
 * @since 2023-09-28
 */
@RestController
@RequestMapping("/sendbackTask")
public class SendbackTaskController {
    @Autowired
    ISendbackTaskService service;

    public void addTask(Task task) {
        System.out.println(task.getId()+" add to Sendback Table");
    }

    public void removeTask(Task task) {
        System.out.println(task.getId()+" remove from Sendback Table");
    }
}
