package com.example.config;

import com.auth0.jwt.JWT;
import com.example.commom.JwtAuthorizeFilter;
import com.example.commom.JwtUtils;
import com.example.commom.RestBean;
import com.example.entity.Account;
import com.example.entity.AuthorizeVO;
import com.example.service.IAccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfiguration {

    @Resource
    JwtUtils jwtUtils;
    @Resource
    JwtAuthorizeFilter jwtAuthorizeFilter;
    @Resource
    IAccountService accountService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                //拦截
                .authorizeHttpRequests(conf -> conf
                                        .requestMatchers("/account/**", "/topic/**", "/topicComment/**").permitAll()
                                        .anyRequest().authenticated())
                //登录
                .formLogin(conf -> conf
                        .loginProcessingUrl("/account/auth/login")
                        .failureHandler(this::onAuthenticationFailure)
                        .successHandler(this::onAuthenticationSuccess))

                //退出
                .logout(conf -> conf
                        .logoutUrl("/account/auth/logout")
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
        AuthorizeVO vo = account.asViewObject(AuthorizeVO.class, v -> {
            v.setExpire(JWT.decode(token).getExpiresAt());
            v.setToken(token);
        });
        response.getWriter().write(RestBean.success(vo).asJsonString());
    }

    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        String authorization = request.getHeader("authorization");
        if (jwtUtils.invalidateJwt(authorization)){
            writer.write(RestBean.success("退出成功").asJsonString());
        }else {
            writer.write(RestBean.failure(400, "退出失败").asJsonString());
        }
    }


}
