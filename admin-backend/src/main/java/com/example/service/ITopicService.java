package com.example.service;

import com.example.entity.Topic;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.TopicVO;
import com.example.entity.TopicVOs;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 杨梦格
 * @since 2024-07-21
 */
public interface ITopicService extends IService<Topic> {

    TopicVOs listTopic(int pageNo, int pageSize, String username, String title, int type);

    Integer getTop(int tid);
}
