package com.example.utils;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.entity.dto.Online;
import com.example.service.impl.OnlineServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;

import java.util.Date;
import java.util.Properties;

@Component
@Slf4j
public class MonitorUtils {
    @Resource
    JwtUtils jwtUtils;
    @Resource
    OnlineServiceImpl service;
    @Resource
    StringRedisTemplate template;
    private final SystemInfo info = new SystemInfo();
    private final Properties properties = System.getProperties();
    public void monitorOnline(String token, HttpServletRequest request){

        DecodedJWT jwt = jwtUtils.resolveJwt("Bearer "+token);

        Online online = new Online();
        OperatingSystem os = info.getOperatingSystem();
        UserDetails user = jwtUtils.toUser(jwt);
        String tokenId = template.opsForValue().get("loginToken:" + jwtUtils.toId(jwt) + ":");

        online.setOnline(1);
        online.setTokenId(tokenId);
        online.setBrowser(this.getBrower(request));
        online.setIpAddr(request.getRemoteAddr());
        online.setOs(properties.getProperty("os.arch"));
        online.setOsName(os.getFamily());
        online.setName(user.getUsername());
        online.setLogintime(new Date());

        if(service.getBaseMapper().selectOne(Wrappers.<Online>query().eq("name", user.getUsername())) == null)
            service.save(online);
        else{
            online.setOnline(1);
            service.update(online, Wrappers.<Online>lambdaUpdate().eq(Online::getName, user.getUsername()));
        }

    }

    /**
     * 获取浏览器版本
     *
     */
    public String getBrower(HttpServletRequest request)
    {
        String browserVersion = null;
        String header = request.getHeader("user-agent");
        if (header.isEmpty())// 为空就默认为谷歌
        {
            browserVersion = "谷歌浏览器";
            return browserVersion;
        }

        if (header.indexOf("Chrome") > 0)// 谷歌
        {
            browserVersion = "谷歌浏览器";
        }
        else if (header.indexOf("Safari") > 0)// safari
        {
            browserVersion = "safari浏览器";
        }
        if (header.indexOf("MSIE") > 0)// ie浏览器
        {
            browserVersion = "ie浏览器";
        }
        if (header.indexOf("Firefox") > 0)// 火狐浏览器
        {
            browserVersion = "火狐浏览器";
        }
        if (header.indexOf("Camino") > 0)//
        {
            browserVersion = "camino浏览器";
        }
        if (header.indexOf("Konqueror") > 0)//
        {
            browserVersion = "konqueror浏览器";
        }
        if (header.indexOf("Quark") > 0)// 夸克浏览器
        {
            browserVersion = "quark浏览器";
        }
        if (header.indexOf("baidu") > 0)// 百度浏览器
        {
            browserVersion = "百度浏览器";
        }
        if (header.indexOf("Edge") > 0)// edge浏览器
        {
            browserVersion = "edge";
        }
        if (header.indexOf("TheWorld") > 0)// theworld浏览器
        {
            browserVersion = "theworld浏览器";
        }
        if (header.indexOf("QQBrowser") > 0 || header.indexOf("TencentTraveler") > 0 || header.indexOf("QQTheme") > 0)// qq浏览器
        {
            browserVersion = "qq浏览器";
        }
        if (header.indexOf("Avast") > 0)// Avast Secure Browser浏览器
        {
            browserVersion = "avast浏览器";
        }
        if (header.indexOf("OPR") > 0)// opera浏览器
        {
            browserVersion = "opera浏览器";
        }
        if (header.indexOf("360") > 0)// 360浏览器
        {
            browserVersion = "360浏览器";
        }
        if (header.indexOf("LBBROWSER") > 0)// 猎豹浏览器
        {
            browserVersion = "猎豹浏览器";
        }
        if (header.indexOf("Maxthon") > 0)// 遨游浏览器
        {
            browserVersion = "遨游浏览器";
        }
        if (header.indexOf("MetaSr") > 0 || header.indexOf("Sogou") > 0)// 搜狗浏览器
        {
            browserVersion = "搜狗浏览器";
        }
        if (header.indexOf("UCWEB") > 0 || header.indexOf("UCBrowser") > 0)// uc浏览器
        {
            browserVersion = "uc浏览器";
        }
        if (browserVersion == null)// 没找到的都默认谷歌浏览器
        {
            browserVersion = "谷歌浏览器";
        }
        return browserVersion;

    }

}
