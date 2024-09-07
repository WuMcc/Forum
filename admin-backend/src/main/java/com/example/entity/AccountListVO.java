package com.example.entity;

import lombok.Data;

import java.util.List;

@Data
public class AccountListVO {
    long count;
    List<Account> rows;
}
