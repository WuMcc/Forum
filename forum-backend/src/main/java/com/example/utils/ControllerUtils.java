package com.example.utils;

import com.example.entity.RestBean;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class ControllerUtils {

    public RestBean<Void> messageHandle(Supplier<String> action){
        String message = action.get();
        if (message == null)
            return RestBean.success();
        else
            return RestBean.failure(400, message);
    }

    public  <T> RestBean<Void> messageHandle(T vo, Function<T, String> function){
        return  messageHandle(() -> function.apply(vo));
    }
}
