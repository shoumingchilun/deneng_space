package com.chilun.deneng.service;

import com.chilun.deneng.pojo.Task;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chilun
 * @since 2023-09-28
 */
public interface ITaskService extends IService<Task> {

    boolean updateByIdWithState(Task task);
}
