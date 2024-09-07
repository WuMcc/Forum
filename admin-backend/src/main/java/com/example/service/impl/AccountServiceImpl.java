package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.commom.RestBean;
import com.example.entity.*;
import com.example.mapper.AccountDetailsMapper;
import com.example.mapper.AccountMapper;
import com.example.mapper.AccountPrivacyMapper;
import com.example.mapper.OnlineMapper;
import com.example.service.IAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 杨梦格
 * @since 2024-07-21
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

    @Resource
    PasswordEncoder encoder;

    @Resource
    StringRedisTemplate template;

    @Resource
    AccountDetailsMapper detailsMapper;

    @Resource
    AccountPrivacyMapper privacyMapper;

    @Resource
    OnlineMapper onlineMapper;

    public UserDetails loadUserByUsername(String username) {
        Account account = this.findAccountByNameOrEmail(username);
        if(account == null)
            throw new UsernameNotFoundException("用户名或密码错误");
        return User
                .withUsername(username)
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }


    @Override
    public Account findAccountByNameOrEmail(String username) {
        Account account = new Account();
        account = this.baseMapper.selectOne(Wrappers.<Account>query().eq("username", username).or().eq("email", username));
        return account;
    }

    @Override
    public Account findAccountById(Integer id) {
        return this.query().eq("id", id).one();
    }

    @Override
    public List<String> getAllRole() {
        List<String> roleList = list().stream().map(Account::getRole).distinct().collect(Collectors.toList());
        return roleList;
    }

    @Override
    public AccountListVO listAllAccount(int pageNo, int pageSize, String username, String email) {
        AccountListVO listAccount = new AccountListVO();
        List<Account> accounts = null;
        Page<Account> page = Page.of(pageNo, pageSize);
        if(StringUtils.hasLength(username) && StringUtils.hasLength(email))
            baseMapper.selectPage(page, Wrappers.<Account>query().like("username", username).like("email", email).orderByAsc("id"));
        else if(StringUtils.hasLength(username))
            baseMapper.selectPage(page, Wrappers.<Account>query().like("username", username).orderByAsc("id"));
        else if(StringUtils.hasLength(email))
            baseMapper.selectPage(page, Wrappers.<Account>query().like("email", email).orderByAsc("id"));
        else
            baseMapper.selectPage(page, Wrappers.<Account>query().orderByAsc("id"));
        accounts = page.getRecords();
        listAccount.setRows(accounts);
        listAccount.setCount(page.getTotal());
        return listAccount;
    }

    @Override
    public String addAccount(Account account) {
        String role = account.getRole();
        String username = account.getUsername();
        String email = account.getEmail();
        String password = encoder.encode(account.getPassword());
        if(existsAccountByEmail(email))
            return "该邮箱已被注册";
        if(existsAccountByUsername(username))
            return "该用户名已被注册";
        Account newAccount = new Account(null, username, password, email, role, "", new Date());
        if(this.save(newAccount)){
            privacyMapper.insert(new AccountPrivacy(newAccount.getId()));
            AccountDetails details = new AccountDetails();
            details.setId(newAccount.getId());
            detailsMapper.insert(details);
            return null;
        }else{
            return "内部错误，请联系管理员";
        }
    }

    @Override
    public String deleteAccount(int id) {
        return this.baseMapper.deleteById(id) == 1 ? "删除用户成功" : null;
    }

    @Override
    public OnlineVO getOnline(int pageNo, int pageSize, String username, String browser) {
        OnlineVO vo = new OnlineVO();
        List<Online> online = new ArrayList<>();
        Page<Online> page = Page.of(pageNo, pageSize);
        if(StringUtils.hasLength(username) && StringUtils.hasLength(browser))
            onlineMapper.selectPage(page, Wrappers.<Online>query().like("name", username).like("browser", browser).orderByAsc("id"));
        else if(StringUtils.hasLength(username))
            onlineMapper.selectPage(page, Wrappers.<Online>query().like("name", username).orderByAsc("id"));
        else if(StringUtils.hasLength(browser))
            onlineMapper.selectPage(page, Wrappers.<Online>query().like("browser", browser).orderByAsc("id"));
        else
            onlineMapper.selectPage(page, Wrappers.<Online>query().orderByAsc("id"));
        online = page.getRecords();
        vo.setRows(online);
        vo.setCount(page.getTotal());
        return vo;
    }


    private boolean existsAccountByEmail(String email){
        return this.baseMapper.exists(Wrappers.<Account>query().eq("email", email));
    }
    private boolean existsAccountByUsername(String username){
        return this.baseMapper.exists(Wrappers.<Account>query().eq("username", username));
    }


}
