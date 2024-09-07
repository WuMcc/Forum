package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.CommentVOs;
import com.example.entity.TopicComment;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 杨梦格
 * @since 2024-07-21
 */
public interface ITopicCommentService extends IService<TopicComment> {
    CommentVOs listComment(int pageNo, int pageSize, String username, String title);
}
