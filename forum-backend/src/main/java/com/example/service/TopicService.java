package com.example.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Interact;
import com.example.entity.dto.Topic;
import com.example.entity.dto.TopicType;
import com.example.entity.vo.request.AddCommentVO;
import com.example.entity.vo.request.TopicCreateVO;
import com.example.entity.vo.request.TopicUpdateVO;
import com.example.entity.vo.response.*;

import java.util.List;

public interface TopicService extends IService<Topic> {
    List<TopicType> listTypes();
    String createTopic(int id, TopicCreateVO vo );
    List<TopicPreviewVO> listTopicByType(int page, int type);
    List<TopTopicVO> topTopic();
    TopicDetailVO getTopic(int tid, int uid);
    void interact(Interact interact, boolean state);
    List<TopicPreviewVO> listTopicCollects(int id);
    String updateTopic(int uid, TopicUpdateVO vo);
    String createComment(int id, AddCommentVO vo);
    List<CommentVO> comments(int tid, int pageNumber);
    void deleteComment(int id, int uid);
    String getWords();
    JSONObject getHistory();
}
