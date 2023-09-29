package com.chilun.deneng.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author chilun
 * @since 2023-09-28
 */
@Getter
@Setter
@TableName("recycled_task")
public class RecycledTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("task_id")
    private Integer taskId;

    private LocalDateTime addTime;
}
