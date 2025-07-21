package com.example.campusmate.repository;

import com.example.campusmate.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 评论表数据库操作接口
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * 根据活动ID查询评论列表
     * @param activityId 活动ID
     * @return 评论列表
     */
    List<Comment> findByActivityId(Long activityId);
}