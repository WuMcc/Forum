package com.example.entity;

import lombok.Data;

import java.util.Date;

@Data
public class CommentVO {
    Integer id;
    String title;
    String username;
    String content;
    Date time;

}
