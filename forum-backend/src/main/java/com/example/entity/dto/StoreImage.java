package com.example.entity.dto;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("db_image_store")
@AllArgsConstructor
@NoArgsConstructor
public class StoreImage {
    Integer uid;
    String name;
    Date time;
}
