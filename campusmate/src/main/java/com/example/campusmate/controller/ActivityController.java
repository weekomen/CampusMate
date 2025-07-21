package com.example.campusmate.controller;

import com.example.campusmate.entity.Activity;
import com.example.campusmate.entity.ActivityApplication;
import com.example.campusmate.service.ActivityService;
import com.example.campusmate.dto.ApplicationDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.example.campusmate.dto.ApiResponse;

@RestController
@RequestMapping("/api/activities")
@CrossOrigin(origins = "*")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping
    public ApiResponse<Activity> createActivity(@RequestBody Activity activity) {
        Activity result = activityService.createActivity(activity);
        return ApiResponse.success(result);
    }

    @GetMapping
    public ApiResponse<List<Activity>> listActivities(
            @RequestParam(required = false) String campus, // 允许前端传递校区参数，非必填
            @RequestParam(required = false) String college, // 允许前端传递学院参数，非必填
            @RequestParam(required = false) String type,   // 允许前端传递活动类型参数，非必填
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Activity> list = activityService.listActivities(campus, college, type, page, size);
        return ApiResponse.success(list);
    }

    @GetMapping("/{activityId}")
    public ApiResponse<Activity> getActivityDetail(@PathVariable Long activityId) {
        Activity activity = activityService.getActivityDetail(activityId);
        return ApiResponse.success(activity);
    }

    @DeleteMapping("/{activityId}")
    public ApiResponse<Boolean> deleteActivity(@PathVariable Long activityId) {
        boolean result = activityService.deleteActivity(activityId);
        return ApiResponse.success(result);
    }

    @PostMapping("/{activityId}/apply")
    public ApiResponse<Boolean> applyForActivity(@PathVariable Long activityId, @RequestParam Long userId, @RequestParam String reason) {
        boolean result = activityService.applyForActivity(activityId, userId, reason);
        return ApiResponse.success(result);
    }

    @GetMapping("/{activityId}/applications")
    public ApiResponse<List<ApplicationDetailDTO>> getActivityApplications(@PathVariable Long activityId) {
        List<ApplicationDetailDTO> applications = activityService.listApplications(activityId);
        return ApiResponse.success(applications);
    }

    @PutMapping("/applications/{appId}/accept")
    public ApiResponse<Boolean> acceptApplication(@PathVariable Long appId) {
        boolean result = activityService.handleApplication(appId, "ACCEPT");
        return ApiResponse.success(result);
    }

    @PutMapping("/applications/{appId}/ignore")
    public ApiResponse<Boolean> ignoreApplication(@PathVariable Long appId) {
        boolean result = activityService.handleApplication(appId, "REJECT");
        return ApiResponse.success(result);
    }
    
    @GetMapping("/user/{userId}/created")
    public ApiResponse<List<Activity>> getUserCreatedActivities(@PathVariable Long userId) {
        List<Activity> activities = activityService.getUserCreatedActivities(userId);
        return ApiResponse.success(activities);
    }
    
    @GetMapping("/user/{userId}/applications")
    public ApiResponse<List<ActivityApplication>> getUserApplications(@PathVariable Long userId) {
        List<ActivityApplication> applications = activityService.getUserApplications(userId);
        return ApiResponse.success(applications);
    }
    
    @GetMapping("/{activityId}/stats")
    public ApiResponse<Map<String, Object>> getActivityStats(@PathVariable Long activityId) {
        Map<String, Object> stats = activityService.getActivityStats(activityId);
        return ApiResponse.success(stats);
    }
    @PostMapping("/{activityId}/like")
    public ApiResponse<Boolean> likeActivity(@PathVariable Long activityId, @RequestParam Long userId) {
        boolean result = activityService.likeActivity(activityId, userId);
        return ApiResponse.success(result);
    }

    @PostMapping("/{activityId}/favorite")
    public ApiResponse<Boolean> favoriteActivity(@PathVariable Long activityId, @RequestParam Long userId) {
        boolean result = activityService.favoriteActivity(activityId, userId);
        return ApiResponse.success(result);
    }

    @GetMapping("/health")
    public ApiResponse<String> health() {
        return ApiResponse.success("ActivityController is working!");
    }

    @GetMapping("/debug-activities")
    public ApiResponse<String> debugActivities() {
        List<Activity> allActivities = activityService.listActivities(null, null, null, 1, 100);
        StringBuilder result = new StringBuilder();
        result.append("数据库中的所有活动:\n");
        
        for (Activity activity : allActivities) {
            result.append(String.format("ID: %d, 标题: %s, 学院: '%s', 校区: '%s', 类型: '%s'\n", 
                activity.getId(), 
                activity.getTitle(), 
                activity.getCollege() != null ? activity.getCollege() : "null", 
                activity.getCampus() != null ? activity.getCampus() : "null", 
                activity.getType() != null ? activity.getType() : "null"));
        }
        
        return ApiResponse.success(result.toString());
    }

}