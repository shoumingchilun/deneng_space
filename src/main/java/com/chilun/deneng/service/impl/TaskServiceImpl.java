package com.chilun.deneng.service.impl;

import com.chilun.deneng.controller.RecycledTaskController;
import com.chilun.deneng.controller.SendbackTaskController;
import com.chilun.deneng.pojo.Task;
import com.chilun.deneng.dao.TaskMapper;
import com.chilun.deneng.service.ITaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chilun
 * @since 2023-09-28
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {
    @Autowired
    RecycledTaskController recycledTaskController;

    @Autowired
    SendbackTaskController sendbackTaskController;

    @Override
    public boolean updateByIdWithState(Task task) {
        TaskMapper mapper = this.getBaseMapper();
        Task oldTask = mapper.selectById(task.getId());
        switch (task.getState()) {
            case 6:
            case 9:
            case 16:
            case 17:
                sendbackTaskController.addTask(task);
                break;
            case 7:
            case 10:
                recycledTaskController.addTask(task);
        }
        switch (oldTask.getState()) {
            case 6:
            case 9:
            case 16:
            case 17:
                sendbackTaskController.removeTask(task);
                break;
            case 7:
            case 10:
                recycledTaskController.removeTask(task);
        }
        return this.updateById(task);
    }
}
