package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author 杨梦格
 * @since 2024-07-21
 */
@TableName("db_account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account implements Serializable, BaseData {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("email")
    private String email;

    @TableField("role")
    private String role = "user";

    @TableField("avatar")
    private String avatar;

    @TableField("register_time")
    private Date registerTime;


}
