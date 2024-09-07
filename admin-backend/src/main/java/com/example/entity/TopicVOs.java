package com.example.entity;

import lombok.Data;

import java.util.List;

@Data
public class TopicVOs {
    int count;
    List<TopicVO> rows;
}
