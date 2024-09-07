package com.example.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.dto.Online;
import com.example.entity.vo.response.AuthorizeVO;
import com.example.filter.JwtAuthorizeFilter;
import com.example.mapper.OnlineMapper;
import com.example.service.AccountService;
import com.example.service.impl.OnlineServiceImpl;
import com.example.utils.JwtUtils;
import com.example.utils.MonitorUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.security.jarsigner.JarSigner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Configuration
public class SecurityConfiguration {

    @Resource
    JwtUtils jwtUtils;
    @Resource
    JwtAuthorizeFilter jwtAuthorizeFilter;
    @Resource
    AccountService accountService;
    @Resource
    StringRedisTemplate template;
    @Resource
    OnlineServiceImpl onlineService;
    @Resource
    MonitorUtils monitorUtils;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                //拦截
                .authorizeHttpRequests(conf -> conf
                                        .requestMatchers("/api/auth/**", "/error", "/api/forum/weather",
                                                "/api/forum/types", "/api/forum/list-topic", "/api/forum/top-topic","/api/forum/topic",
                                                "/api/forum/comments","/api/forum/words","/api/forum/history","/api/forum/notice").permitAll()
                                        .requestMatchers("/images/**").permitAll()
                                        .anyRequest().authenticated())
                //登录
                .formLogin(conf -> conf
                        .loginProcessingUrl("/api/auth/login")
                        .failureHandler(this::onAuthenticationFailure)
                        .successHandler(this::onAuthenticationSuccess))

                //退出
                .logout(conf -> conf
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(this::onLogoutSuccess))

                //jwt异常处理
                .exceptionHandling(conf -> conf
                        .authenticationEntryPoint(this::onUnauthorized)
                        .accessDeniedHandler(this::onAccessDeny))
                //跨域
                .csrf(AbstractHttpConfigurer::disable)
                //设置session无状态
                .sessionManagement(conf -> conf
                                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //添加过滤器,放在默认之前
                .addFilterBefore(jwtAuthorizeFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    public void onAccessDeny(HttpServletRequest request,
                             HttpServletResponse response,
                             AccessDeniedException exception) throws IOException{
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(RestBean.forbidden(exception.getMessage()).asJsonString());
    }

    public void onUnauthorized(HttpServletRequest request,
                                HttpServletResponse response,
                                AuthenticationException exception) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(RestBean.unauthorized(exception.getMessage()).asJsonString());
    }

    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(RestBean.unauthorized(exception.getMessage()).asJsonString());
    }

    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        User user = (User) authentication.getPrincipal();
        Account account = accountService.findAccountByNameOrEmail(user.getUsername());
        String token = jwtUtils.createJwt(user, account.getId(),  account.getUsername());
        int id = jwtUtils.toId(jwtUtils.resolveJwt("Bearer "+token));
        //第一次创建uuid
        template.opsForValue().set("loginToken:" + id + ":", getUuid(), 7, TimeUnit.DAYS);
        AuthorizeVO vo = account.asViewObject(AuthorizeVO.class, v -> {
            v.setExpire(JWT.decode(token).getExpiresAt());
            v.setToken(token);
        });
        monitorUtils.monitorOnline(token, request);
        response.getWriter().write(RestBean.success(vo).asJsonString());
    }

    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        String authorization = request.getHeader("authorization");
        if(request.getHeader("name") != null)
        {
            Account account = accountService.findAccountByNameOrEmail(request.getHeader("name"));
            authorization = "Bearer "+template.opsForValue().get("Token:" + account.getId() + ":");
        }
        DecodedJWT jwt = jwtUtils.resolveJwt(authorization);
        User user = (User) jwtUtils.toUser(jwt);
        int id = jwtUtils.toId(jwt);
        if (jwtUtils.invalidateJwt(authorization)){
            if(Boolean.TRUE.equals(template.delete("loginToken:" + id + ":"))){
                onlineService.update().set("online", 0).set("logouttime", new Date()).eq("name", user.getUsername()).update();
            }
            writer.write(RestBean.success("退出成功").asJsonString());
        }else {
            writer.write(RestBean.failure(400, "退出失败").asJsonString());
        }
    }
    private String getUuid(){
        return UUID.randomUUID().toString();
    }


}
