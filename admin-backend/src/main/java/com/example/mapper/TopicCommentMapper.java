package com.example.mapper;

import com.example.entity.CommentVO;
import com.example.entity.TopicComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 杨梦格
 * @since 2024-07-21
 */@Mapper
public interface TopicCommentMapper extends BaseMapper<TopicComment> {

     @Select("""
            select c.id, title, username, c.content, c.time 
            from db_topic_comment c 
            join db_topic t on c.tid = t.id
            join db_account a on c.uid = a.id
            where t.title like concat('%', #{title}, '%')
            and a.username like concat('%', #{username}, '%')
            """)
     List<CommentVO> getCommentByTitleAndUsername(@Param("username") String username, @Param("title") String title);

     @Select("""
            select c.id, title, username, c.content, c.time 
            from db_topic_comment c 
            join db_topic t on c.tid = t.id
            join db_account a on c.uid = a.id
            where a.username like concat('%', #{username}, '%')
            """)
     List<CommentVO> getCommentByUsername(@Param("username") String username);

     @Select("""
            select c.id, title, username, c.content, c.time 
            from db_topic_comment c 
            join db_topic t on c.tid = t.id
            join db_account a on c.uid = a.id
            where t.title like concat('%', #{title}, '%')
            """)
     List<CommentVO> getCommentByTitle(@Param("title") String title);

     @Select("""
            select c.id, title, username, c.content, c.time 
            from db_topic_comment c 
            join db_topic t on c.tid = t.id
            join db_account a on c.uid = a.id
            """)
     List<CommentVO> getComment();
}
