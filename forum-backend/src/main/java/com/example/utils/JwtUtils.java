package com.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {

    @Value("${spring.security.jwt.key}")
    String key;

    @Value("${spring.security.jwt.expire}")
    int expire;

    @Resource
    StringRedisTemplate template;

    //令牌失效方法
    public boolean invalidateJwt(String headerToken){
        String token = this.convertToken(headerToken);
        if(token == null) return false;
        //获取相同的key，指定相同的jwt算法
        Algorithm algorithm = Algorithm.HMAC256(key);
        //创建验证器，用于验证JWT合法性
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            //设置黑名单，通过jwt的id来判断，因此在下面create的时候加入uuid生成的id
            DecodedJWT jwt = jwtVerifier.verify(token);
            String id = jwt.getId();
            return deleteToken(id, jwt.getExpiresAt());
        }catch (JWTVerificationException e){
            return false;
        }
    }

    //让令牌失效
    private boolean deleteToken(String uuid, Date time){
        if (this.isInvalidToken(uuid))
            return false;
        Date now = new Date();
        //设置过期时间
        long expire = Math.max(time.getTime() - now.getTime(), 0);
        //存入redis
        template.opsForValue().set(Const.JWT_BLACK_LIST + uuid, "", expire, TimeUnit.MILLISECONDS);
        return true;
    }

    //判断是否失效
    private boolean isInvalidToken(String uuid){
        return Boolean.TRUE.equals(template.hasKey(Const.JWT_BLACK_LIST + uuid));
    }


    //解析Jwt
    public DecodedJWT resolveJwt(String headerToken){
        String token = this.convertToken(headerToken);
        if(token == null) return null;
        //获取相同的key，指定相同的jwt算法
        Algorithm algorithm = Algorithm.HMAC256(key);
        //创建验证器，用于验证JWT合法性
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try{
            DecodedJWT verify = jwtVerifier.verify(token);
            if (this.isInvalidToken(verify.getId()))
                return null;
            Date expiresAt = verify.getExpiresAt();
            //判断过期时间
            return new Date().after(expiresAt) ? null : verify;
        }catch (JWTVerificationException e){
            return null;
        }
    }



    //创建JWT
    public String createJwt(UserDetails details, int id, String username){
        Algorithm algorithm = Algorithm.HMAC256(key);
        Date expire = this.expireTime();
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("id", id)    //自定义存入id
                .withClaim("name", username)    //自定义存入name
                .withClaim("authorities", details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())   //自定义存入验证信息
                .withExpiresAt(expire)  //设置过期时间
                .withIssuedAt(new Date())   //设置颁发时间
                .sign(algorithm);   //签名
    }

    //过期时间计算
    public Date expireTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, expire * 24);
        return calendar.getTime();
    }

    //将JWT解析为UserDetails
    public UserDetails toUser(DecodedJWT jwt){
        Map<String, Claim> claims = jwt.getClaims();
        return User
                .withUsername(claims.get("name").asString())
                .password("*****")
                .authorities(claims.get("authorities").asArray(String.class))
                .build();
    }

    //将JWT解析为id
    public Integer toId(DecodedJWT jwt){
        Map<String, Claim> claims = jwt.getClaims();
        return claims.get("id").asInt();
    }


    //判断token是否合法，合法即切割开头自带Bearer返回
    private String convertToken(String headerToken){
        if(headerToken == null || !headerToken.startsWith("Bearer "))
            return null;
        return headerToken.substring(7);
    }

}
