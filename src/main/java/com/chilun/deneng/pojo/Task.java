package com.chilun.deneng.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer poolId;
    private Integer state;
    private Integer labelUserId;
    private Integer qualityUserId;
    private Integer finalUserId;
    private String exampleQuestion;
    private String exampleAnswer;
    private String originQuestion;
    private String originAnswer;
    private String question;
    private String answer;
    @TableField("`history`")
    private String history;
    private String deleteReason;
    private String backReason;
    private Integer secondBackLabel;
    private Integer secendBackQuality;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastChangedTime;
}
