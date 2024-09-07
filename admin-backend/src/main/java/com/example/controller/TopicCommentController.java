package com.example.controller;

import com.example.commom.RestBean;
import com.example.entity.CommentVO;
import com.example.entity.CommentVOs;
import com.example.mapper.TopicCommentMapper;
import com.example.service.ITopicCommentService;
import jakarta.annotation.Resource;
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
@RequestMapping("/topicComment")
public class TopicCommentController {
    @Resource
    TopicCommentMapper mapper;
    @Resource
    ITopicCommentService service;

    @GetMapping("/list")
    public RestBean<CommentVOs> getComment(@RequestParam(value = "pageNo") int pageNo,
                                           @RequestParam(value = "pageSize") int pageSize,
                                           @RequestParam(required = false) String username,
                                           @RequestParam(required = false) String title) {
        CommentVOs vo = service.listComment(pageNo, pageSize, username, title);
        return vo != null ? RestBean.success(vo) : RestBean.failure(400, "获取失败");
    }

    @PostMapping("/delete")
    public RestBean<Void> deleteComment(@RequestParam int id){
        return service.removeById(id) ? RestBean.success("删除成功") : RestBean.failure(400, "删除失败");
    }
}
