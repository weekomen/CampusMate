package com.example.campusmate.service.impl;

import com.example.campusmate.entity.Comment;
import com.example.campusmate.repository.CommentRepository;
import com.example.campusmate.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment addComment(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsByActivity(Long activityId) {
        return commentRepository.findByActivityId(activityId);
    }

    @Override
    public boolean deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
        return true;
    }
}