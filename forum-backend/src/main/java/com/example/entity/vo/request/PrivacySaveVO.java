package com.example.entity.vo.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PrivacySaveVO {
    //勾选哪个就传回来哪个，每次勾选一次性传回来浪费性能
    @Pattern(regexp = "(phone|email|qq|wx|gender)")
    String type;
    boolean status;
}
