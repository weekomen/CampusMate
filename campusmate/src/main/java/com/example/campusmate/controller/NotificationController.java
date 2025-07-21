package com.example.campusmate.controller;

import com.example.campusmate.entity.Notification;
import com.example.campusmate.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.campusmate.dto.ApiResponse;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ApiResponse<List<Notification>> listNotifications(@RequestParam Long userId, @RequestParam(required = false) Integer isRead) {
        List<Notification> list = notificationService.listNotifications(userId, isRead);
        return ApiResponse.success(list);
    }

    @PutMapping("/{notifyId}/read")
    public ApiResponse<Boolean> markAsRead(@PathVariable Long notifyId) {
        boolean result = notificationService.markAsRead(notifyId);
        return ApiResponse.success(result);
    }

    @DeleteMapping("/{notifyId}")
    public ApiResponse<Boolean> deleteNotification(@PathVariable Long notifyId) {
        boolean result = notificationService.deleteNotification(notifyId);
        return ApiResponse.success(result);
    }
}