package com.example.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TopicVO {
    int type;
    String title;
    String text;
    Date time;
    Integer tid;
    Integer uid;
    List<String> images;
    String username;
    String avatar;
}
