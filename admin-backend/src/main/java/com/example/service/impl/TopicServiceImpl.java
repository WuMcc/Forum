package com.example.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.*;
import com.example.mapper.AccountMapper;
import com.example.mapper.TopicMapper;
import com.example.service.ITopicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import net.sf.jsqlparser.statement.select.Top;
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
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements ITopicService {

    @Resource
    AccountMapper accountMapper;
    @Override
    public TopicVOs listTopic(int pageNo, int pageSize, String username, String title, int type) {
        List<TopicVO> vo = new ArrayList<>();
        List<Topic> topicList = new ArrayList<>();
        Page<Topic> page = Page.of(pageNo, pageSize);
        int uid = 0;
        if(StringUtils.hasLength(username)){
            Account account = accountMapper.selectOne(Wrappers.<Account>query().eq("username", username));
            uid = account.getId();
        }
        if(StringUtils.hasLength(username) && StringUtils.hasLength(title) && type != 0)
            baseMapper.selectPage(page, Wrappers.<Topic>query().eq("uid", uid).like("title", title).eq("type", type).orderByAsc("id"));
        else if(StringUtils.hasLength(username))
            baseMapper.selectPage(page, Wrappers.<Topic>query().eq("uid", uid).orderByAsc("id"));
        else if(StringUtils.hasLength(title))
            baseMapper.selectPage(page, Wrappers.<Topic>query().like("title", title).orderByAsc("id"));
        else if(type != 0)
            baseMapper.selectPage(page, Wrappers.<Topic>query().eq("type", type).orderByAsc("id"));
        else
            baseMapper.selectPage(page, Wrappers.<Topic>query().orderByAsc("id"));
        topicList = page.getRecords();
        if(topicList.isEmpty()) return null;
        vo = topicList.stream().map(this::resolveVO).toList();
        TopicVOs vos = new TopicVOs();
        vos.setCount((int) page.getTotal());
        vos.setRows(vo);
        return vos;
    }

    @Override
    public Integer getTop(int tid) {
        return this.baseMapper.selectById(tid).getTop();
    }


    private TopicVO resolveVO(Topic topic){
        TopicVO vo = new TopicVO();
        BeanUtils.copyProperties(accountMapper.selectById(topic.getUid()), vo);
        BeanUtils.copyProperties(topic, vo);
        List<String> images = new ArrayList<>();
        StringBuilder previewText = new StringBuilder();
        JSONArray ops = JSONObject.parseObject(topic.getContent()).getJSONArray("ops");
        this.shortContent(ops, previewText, obj -> images.add(obj.toString()));
        vo.setText(previewText.length() > 10 ? previewText.substring(0, 10) + "..." : previewText.toString());
        vo.setImages(images);
        return vo;
    }

    private void shortContent(JSONArray ops, StringBuilder previewText, Consumer<Object> imageHandler){
        for(Object op : ops){
            Object insert = JSONObject.from(op).get("insert");
            if(insert instanceof String text){
                if(previewText.length() >= 10) continue;
                previewText.append(text);
            }else if(insert instanceof Map<?,?> map){
                Optional.ofNullable(map.get("image")).ifPresent(imageHandler);
            }
        }
    }

    private boolean textLimitCheck(JSONObject object, int max){
        if(object == null) return false;
        long length = 0;
        for (Object op : object.getJSONArray("ops")) {
            length += JSONObject.from(op).getString("insert").length();
        }
        return length <= max;
    }
}
