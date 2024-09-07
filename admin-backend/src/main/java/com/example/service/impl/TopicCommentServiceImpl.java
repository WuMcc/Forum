package com.example.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.*;
import com.example.mapper.TopicCommentMapper;
import com.example.service.ITopicCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 杨梦格
 * @since 2024-07-21
 */
@Service
public class TopicCommentServiceImpl extends ServiceImpl<TopicCommentMapper, TopicComment> implements ITopicCommentService {

    @Resource
    TopicCommentMapper mapper;

    @Override
    public CommentVOs listComment(int pageNo, int pageSize, String username, String title) {
        Page<CommentVO> page = Page.of(pageNo, pageSize);
        List<CommentVO> vo = new ArrayList<>();
        if(StringUtils.hasLength(username) && StringUtils.hasLength(title))
            vo = mapper.getCommentByTitleAndUsername(username, title);
        else if(StringUtils.hasLength(username))
            vo = mapper.getCommentByUsername(username);
        else if(StringUtils.hasLength(title))
            vo = mapper.getCommentByTitle(title);
        else
            vo = mapper.getComment();
        int start = (pageNo - 1) * pageSize;
        int end = Math.min(start + pageSize, vo.size());
        List<CommentVO> records = vo.subList(start, end);
        records = records.stream().map(this::resolveVO).toList();
        CommentVOs vos = new CommentVOs();
        vos.setRows(records);
        vos.setCount(vo.size());
        return vos;
    }


    private CommentVO resolveVO(CommentVO comment){
        CommentVO vo = new CommentVO();
        BeanUtils.copyProperties(comment, vo);
        StringBuilder previewText = new StringBuilder();
        JSONArray ops = JSONObject.parseObject(comment.getContent()).getJSONArray("ops");
        this.shortContent(ops, previewText);
        vo.setContent(previewText.length() > 20 ? previewText.substring(0, 20) + "..." : previewText.toString());
        return vo;
    }

    private void shortContent(JSONArray ops, StringBuilder previewText){
        for(Object op : ops){
            Object insert = JSONObject.from(op).get("insert");
            if(insert instanceof String text){
                if(previewText.length() >= 10) continue;
                previewText.append(text);
            }
        }
    }
}
