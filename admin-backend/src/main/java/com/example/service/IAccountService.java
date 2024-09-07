package com.example.service;

import com.example.commom.RestBean;
import com.example.entity.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.AccountListVO;
import com.example.entity.Online;
import com.example.entity.OnlineVO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 杨梦格
 * @since 2024-07-21
 */
public interface IAccountService extends IService<Account>, UserDetailsService {

    Account findAccountByNameOrEmail(String username);
    Account findAccountById(Integer id);
    List<String> getAllRole();

    AccountListVO listAllAccount(int pageNo, int pageSize, String username, String email);

    String addAccount(Account account);

    String deleteAccount(int id);
    OnlineVO getOnline(int pageNo, int pageSize, String username, String browser);
}
