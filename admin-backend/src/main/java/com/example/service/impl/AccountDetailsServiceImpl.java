package com.example.service.impl;

import com.example.entity.AccountDetails;
import com.example.mapper.AccountDetailsMapper;
import com.example.service.IAccountDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 杨梦格
 * @since 2024-07-21
 */
@Service
public class AccountDetailsServiceImpl extends ServiceImpl<AccountDetailsMapper, AccountDetails> implements IAccountDetailsService {

}
