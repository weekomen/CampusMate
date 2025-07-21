package com.example.campusmate.controller;

import com.example.campusmate.entity.UserInfo;
import com.example.campusmate.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.campusmate.dto.ApiResponse;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/{userId}")
    public ApiResponse<UserInfo> getUserInfo(@PathVariable Long userId) {
        UserInfo userInfo = userInfoService.getUserInfo(userId);
        return ApiResponse.success(userInfo);
    }

    @PutMapping("/{userId}")
    public ApiResponse<Boolean> updateUserInfo(@PathVariable Long userId, @RequestBody UserInfo userInfo) {
        boolean result = userInfoService.updateUserInfo(userId, userInfo);
        return ApiResponse.success(result);
    }

    @PutMapping("/{userId}/settings")
    public ApiResponse<Boolean> updateVisibility(@PathVariable Long userId, @RequestBody String visibility) {
        boolean result = userInfoService.updateVisibility(userId, visibility);
        return ApiResponse.success(result);
    }
}
