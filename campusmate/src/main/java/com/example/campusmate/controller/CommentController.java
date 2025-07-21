package com.example.campusmate.controller;

import com.example.campusmate.entity.Comment;
import com.example.campusmate.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.campusmate.dto.ApiResponse;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ApiResponse<Comment> addComment(@RequestBody Comment comment) {
        Comment result = commentService.addComment(comment);
        return ApiResponse.success(result);
    }

    @GetMapping("/activity/{activityId}")
    public ApiResponse<List<Comment>> getCommentsByActivity(@PathVariable Long activityId) {
        List<Comment> list = commentService.getCommentsByActivity(activityId);
        return ApiResponse.success(list);
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<Boolean> deleteComment(@PathVariable Long commentId) {
        boolean result = commentService.deleteComment(commentId);
        return ApiResponse.success(result);
    }
}