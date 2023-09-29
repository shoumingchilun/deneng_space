package com.chilun.deneng.dao;

import com.chilun.deneng.pojo.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chilun
 * @since 2023-09-28
 */
public interface TaskMapper extends BaseMapper<Task> {
    void updateTaskWithAtom(@Param("task") Task task,@Param("oldTask") Task oldTask);
}
