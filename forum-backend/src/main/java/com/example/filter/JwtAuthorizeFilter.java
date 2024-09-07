package com.example.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.entity.RestBean;
import com.example.entity.dto.Online;
import com.example.mapper.OnlineMapper;
import com.example.service.impl.OnlineServiceImpl;
import com.example.utils.Const;
import com.example.utils.JwtUtils;
import com.example.utils.MonitorUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class JwtAuthorizeFilter extends OncePerRequestFilter {

    @Resource
    JwtUtils jwtUtils;
    @Resource
    StringRedisTemplate template;
    @Resource
    OnlineServiceImpl service;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        DecodedJWT jwt = jwtUtils.resolveJwt(authorization);
        if(jwt != null) {
            User use = (User) jwtUtils.toUser(jwt);
            Online online = service.getOne(Wrappers.<Online>query().eq("name", use.getUsername()));
            if (online.getOnline() == 1)
                {
                    //jwt不为空，将用户信息解析出来放入UserDetails
                    UserDetails user = jwtUtils.toUser(jwt);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    request.setAttribute(Const.ATTR_USER_ID, jwtUtils.toId(jwt));
                }
        }
        filterChain.doFilter(request, response);
    }

}
