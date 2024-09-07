package com.example.controller;

import com.example.commom.Const;
import com.example.commom.RestBean;
import com.example.entity.Account;
import com.example.entity.AccountListVO;
import com.example.entity.Online;
import com.example.entity.OnlineVO;
import com.example.service.IAccountService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 杨梦格
 * @since 2024-07-21
 */
@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

    @Resource
    IAccountService accountService;

    @GetMapping("/list")
    public RestBean<AccountListVO> getAllAccount(@RequestParam(value = "pageNo") int pageNo,
                                                       @RequestParam(value = "pageSize") int pageSize,
                                                       @RequestParam(required = false) String username,
                                                       @RequestParam(required = false) String email){
        return RestBean.success(accountService.listAllAccount(pageNo, pageSize, username, email));
    }

    @GetMapping("/one")
    public RestBean<Account> getAccount(@RequestParam int id){
        return RestBean.success(accountService.findAccountById(id));
    }

    @PostMapping("/update")
    public RestBean<Void> updateAccount(@RequestBody Account account){
        boolean b = accountService.updateById(account);
        return b ? RestBean.success() : RestBean.failure(400, "更新失败");
    }


    @GetMapping("/info")
    public RestBean<Account> getInfo(@RequestAttribute(Const.ATTR_USER_ID) int uid){
        Account account = accountService.findAccountById(uid);
        return RestBean.success(account);
    }
    @GetMapping("/role/list")
    public RestBean<List<String>> getAllRoleList(){
        return RestBean.success(accountService.getAllRole());
    }

    @PostMapping("/add")
    public RestBean<Void> addAccount(@RequestBody Account account){
        String msg = accountService.addAccount(account);
        return msg != null ? RestBean.failure(400, msg) :RestBean.success();
    }

    @GetMapping("/delete")
    public RestBean<Void> deleteAccount(@RequestParam int id){
        String msg = accountService.deleteAccount(id);
        return msg == null ? RestBean.failure(400, "删除失败，请联系管理员") :RestBean.success(msg);
    }

    @GetMapping("/online")
    public RestBean<OnlineVO> getOnline(@RequestParam(value = "pageNo") int pageNo,
                                        @RequestParam(value = "pageSize") int pageSize,
                                        @RequestParam(required = false) String username,
                                        @RequestParam(required = false) String browser){
        return RestBean.success(accountService.getOnline(pageNo, pageSize, username, browser));
    }



}
