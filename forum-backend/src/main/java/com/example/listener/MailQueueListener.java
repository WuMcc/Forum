package com.example.listener;

import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "mail")
public class MailQueueListener {
    @Resource
    JavaMailSender sender;

    @Value("${spring.mail.username}")
    String username;

    @RabbitHandler
    public void sendMailMessage(Map<String, Object> data){
        String email = data.get("email").toString();
        Integer code = (Integer) data.get("code");
        String type = data.get("type").toString();
        SimpleMailMessage message = switch (type){
            case "register" -> creatMessage("正在注册注册", "您的验证码是：" + code + ", 有效时间1分钟, 打死不要告诉其他人！", email);
            case "reset" -> creatMessage("正在重置密码", "您的验证码是：" + code + ", 有效时间1分钟, 打死不要告诉其他人！", email);
            case "modify" -> creatMessage("正在修改绑定邮箱", "您的验证码是：" + code + ", 有效时间1分钟, 打死不要告诉其他人！", email);
            default -> null;
        };
        if(message == null) return;
        sender.send(message);
    }

    private SimpleMailMessage creatMessage(String title, String content, String email){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(title);
        message.setText(content);
        message.setTo(email);
        message.setFrom(username);
        return message;
    }
}
