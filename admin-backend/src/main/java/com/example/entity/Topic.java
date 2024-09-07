package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 杨梦格
 * @since 2024-07-21
 */
@Getter
@Setter
@TableName("db_topic")
public class Topic implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer tid;

    @TableField("title")
    private String title;

    @TableField("content")
    private String content;

    @TableField("uid")
    private Integer uid;

    @TableField("type")
    private Integer type;

    @TableField("update_time")
    private Date updateTime;

    @TableField("time")
    private Date time;

    @TableField("top")
    private Integer top;


}
