package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@TableName("db_online")
@Accessors(chain = true)
public class Online {
    @TableId(value = "id",type = IdType.AUTO)
    Integer id;
    @TableField(value = "tokenId")
    String tokenId;
    String name;
    @TableField(value = "ipaddr")
    String ipAddr;
    String browser;
    String os;
    @TableField(value = "osname")
    String osName;
    Integer online;
    @TableField(value = "logintime")
    Date logintime;
    @TableField(value = "logouttime")
    Date logouttime;
}
