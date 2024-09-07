package com.example.entity;

import lombok.Data;

import java.util.List;

@Data
public class CommentVOs {
    long count;
    List<CommentVO> rows;
}
