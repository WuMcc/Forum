package com.example.entity;

import lombok.Data;

import java.util.List;

@Data
public class OnlineVO {
    long count;
    List<Online> rows;
}
