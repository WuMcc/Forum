package com.example.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.commom.RestBean;
import com.example.entity.*;
import com.example.mapper.NoticeMapper;
import com.example.mapper.OnlineMapper;
import com.example.mapper.TopicMapper;
import com.example.service.ITopicService;
import jakarta.annotation.Resource;
import net.sf.jsqlparser.statement.select.Top;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 杨梦格
 * @since 2024-07-21
 */
@RestController
@RequestMapping("/topic")
public class TopicController {
    @Resource
    ITopicService service;
    @Resource
    TopicMapper mapper;
    @Resource
    NoticeMapper noticeMapper;
    @Resource
    OnlineMapper onlineMapper;

    @GetMapping("/list")
    public RestBean<TopicVOs> listTopic(@RequestParam(value = "pageNo") int pageNo,
                                       @RequestParam(value = "pageSize") int pageSize,
                                       @RequestParam(required = false) String username,
                                       @RequestParam(required = false) String title,
                                       @RequestParam int type){
        return RestBean.success(service.listTopic(pageNo, pageSize, username, title, type));
    }

    @GetMapping("/top")
    public RestBean<Integer> getTop(@RequestParam int tid){
        return RestBean.success(service.getTop(tid));
    }

    @PostMapping("/top")
    public RestBean<String> saveTop(@RequestParam int id, @RequestParam int top){
        Topic topic = mapper.selectById(id);
        topic.setTop(top);
        return service.saveOrUpdate(topic) ? RestBean.success("操作成功！") : RestBean.failure(401, "置顶失败");
    }

    @PostMapping("/delete")
    public RestBean<String> deleteTopic(@RequestParam int id){
        return service.removeById(id) ? RestBean.success("删除成功！") : RestBean.failure(401, "删除失败");
    }

    @PostMapping("/notice")
    public RestBean<Void> notice(@RequestBody NoticeVO vo){
        Notice notice = new Notice();
        BeanUtils.copyProperties(vo, notice);
        return noticeMapper.updateById(notice) == 1 ? RestBean.success("发布成功！") : RestBean.failure(400, "发布失败");
    }

    @GetMapping("/logout")
    public RestBean<Void> logout(@RequestParam String name){
        Online online = onlineMapper.selectOne(Wrappers.<Online>query().eq("name", name));
        online.setOnline(0);
        onlineMapper.updateById(online);
        return RestBean.success("退出成功！");
    }
}
