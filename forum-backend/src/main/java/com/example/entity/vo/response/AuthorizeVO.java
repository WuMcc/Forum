package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * 登陆成功后的响应类
 * 返回用户名、角色、token、过期时间
 */

@Data
public class AuthorizeVO {
    String username;
    String role;
    String token;
    Date expire;
}
