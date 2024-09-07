package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
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
@TableName("db_account_details")
public class AccountDetails implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("gender")
    private Integer gender;

    @TableField("phone")
    private String phone;

    @TableField("qq")
    private String qq;

    @TableField("wx")
    private String wx;

    @TableField("desc")
    private String desc;


}
