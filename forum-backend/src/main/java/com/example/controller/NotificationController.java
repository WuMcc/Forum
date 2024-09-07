package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.response.NotificationVO;
import com.example.service.NotificationService;
import com.example.utils.Const;
import com.example.utils.ControllerUtils;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Resource
    NotificationService notificationService;

    @GetMapping("/list")
    public RestBean<List<NotificationVO>> listNotification(@RequestAttribute(Const.ATTR_USER_ID) int uid){
        return RestBean.success(notificationService.findUserNotifications(uid));
    }

    @GetMapping("/delete-all")
    public RestBean<Void> deleteAll(@RequestAttribute(Const.ATTR_USER_ID) int uid) {
        notificationService.deleteUserAllNotification(uid);
        return RestBean.success();
    }

    @GetMapping("/delete")
    public RestBean<Void> delete(@RequestParam @Min(0) int id,
                                 @RequestAttribute(Const.ATTR_USER_ID) int uid) {
        notificationService.deleteUserNotification(id, uid);
        return RestBean.success();
    }
}
