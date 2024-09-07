package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Account;
import com.example.entity.dto.AccountDetails;
import com.example.entity.dto.AccountPrivacy;
import com.example.entity.vo.request.*;
import com.example.mapper.AccountDetailsMapper;
import com.example.mapper.AccountMapper;
import com.example.mapper.AccountPrivacyMapper;
import com.example.service.AccountService;
import com.example.utils.Const;
import com.example.utils.FlowUtils;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Resource
    AmqpTemplate amqpTemplate;

    @Resource
    StringRedisTemplate template;

    @Resource
    FlowUtils flowUtils;

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    AccountPrivacyMapper privacyMapper;

    @Resource
    AccountDetailsMapper detailsMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = this.findAccountByNameOrEmail(username);
        if (account == null)
            throw new UsernameNotFoundException("用户名或密码错误");
        return User
                .withUsername(username)
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    public Account findAccountByNameOrEmail(String text){
        return this.query()
                .eq("username", text).or()
                .eq("email", text)
                .one();
    }

    @Override
    public String registerEmailVerifyCode(String type, String email, String ip) {
        synchronized (ip.intern()){
            if (!this.verifyLimit(ip)){
                return "请求频繁，请稍后再试！";
            }
            Random random = new Random();
            int code = random.nextInt(899999) + 100000;
            Map<String, Object> data = Map.of("type", type, "email", email, "code", code);
            amqpTemplate.convertAndSend("mail", data);
            template.opsForValue()
                    .set(this.getKey(email), String.valueOf(code), 1, TimeUnit.MINUTES);
            return null;
        }

    }

    @Override
    public String registerEmailAccount(EmailRegisterVO vo) {
        String email = vo.getEmail();
        String username = vo.getUsername();
        String code = template.opsForValue().get(this.getKey(email));
        if (vo.getCode() == null) return "请先获取验证码";
        if(code == null) return "验证码已失效，请重新获取";
        if(!code.equals(vo.getCode())) return "验证码输入错误，请重新输入";
        if (this.existsAccountByEmail(email)) return "此电子邮件已被其他用户注册";
        if(this.existsAccountByUsername(username)) return "此用户名已被其他用户使用";
        String password = passwordEncoder.encode(vo.getPassword());
        Account account = new Account(null, username, password, email, "user", "", new Date());
        if(this.save(account)){
            template.delete(this.getKey(email));
            privacyMapper.insert(new AccountPrivacy(account.getId()));
            AccountDetails details = new AccountDetails();
            details.setId(account.getId());
            detailsMapper.insert(details);
            return null;
        }else{
            return "内部错误，请联系管理员";
        }
    }

    @Override
    public String resetVerifyCode(ResetVO vo) {
        String code = template.opsForValue().get(this.getKey(vo.getEmail()));
        if (vo.getCode() == null) return "请先获取验证码";
        if(code == null) return "验证码已失效，请重新获取";
        if(!code.equals(vo.getCode())) return "验证码输入错误，请重新输入";
        return null;
    }

    @Override
    public String resetPassword(EmailResetVO vo) {
        String verify = this.resetVerifyCode(new ResetVO(vo.getEmail(), vo.getCode()));
        if (verify != null) return verify;
        String password = passwordEncoder.encode(vo.getPassword());
        boolean update = this.update().eq("email", vo.getEmail()).set("password", password).update();
        if (update){
            template.delete(this.getKey(vo.getEmail()));
        }
        return null;
    }

    @Override
    public Account findAccountById(int id) {
        return this.query().eq("id", id).one();
    }

    @Override
    public String modifyEmail(int id, ModifyEmailVO vo) {
        String code = template.opsForValue().get(this.getKey(vo.getEmail()));
        if (vo.getCode() == null) return "请先获取验证码";
        if(code == null) return "验证码已失效，请重新获取";
        if(!code.equals(vo.getCode())) return "验证码输入错误，请重新输入";
        template.delete(this.getKey(vo.getEmail()));
        Account account = this.findAccountByNameOrEmail(vo.getEmail());
        if(account != null && account.getId() != id)
            return "此电子邮件已被其他用户注册";
        if(account != null && account.getEmail().equals(vo.getEmail()))
            return "当前用户已绑定此邮箱";
        this.update()
                .set("email", vo.getEmail())
                .eq("id", id)
                .update();
        return null;
    }

    @Override
    public String changePassword(int id, ChangePasswordVO vo) {
        String password = this.query()
                .eq("id", id)
                .one()
                .getPassword();
        if(!passwordEncoder.matches(vo.getPassword(), password)){
            return "原密码错误，请重新输入密码!";
        }
        boolean success = this.update()
                .eq("id", id)
                .set("password", passwordEncoder.encode(vo.getNew_password()))
                .update();
        return success ? null : "内部错误，请联系管理员";
    }

    private boolean existsAccountByEmail(String email){
        return this.baseMapper.exists(Wrappers.<Account>query().eq("email", email));
    }
    private boolean existsAccountByUsername(String username){
        return this.baseMapper.exists(Wrappers.<Account>query().eq("username", username));
    }

    private boolean verifyLimit(String ip){
        String key = Const.VERIFY_EMAIL_LIMIT + ip;
        return flowUtils.limitOnceCheck(key, 60);
    }

    private String getKey(String email){
        return Const.VERIFY_EMAIL_DATA + email;
    }
}
