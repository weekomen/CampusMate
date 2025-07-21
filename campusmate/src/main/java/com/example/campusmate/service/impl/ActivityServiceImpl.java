package com.example.campusmate.service.impl;

import com.example.campusmate.entity.Activity;
import com.example.campusmate.repository.ActivityRepository;
import com.example.campusmate.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.example.campusmate.dto.ApplicationDetailDTO;
import com.example.campusmate.entity.ActivityApplication;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public Activity createActivity(Activity activity) {
        activity.setCreatedAt(LocalDateTime.now());
        activity.setStatus("PUBLISHED");
        return activityRepository.save(activity);
    }

    @Override
    public List<Activity> listActivities(String campus, String college, String type, int page, int size) {
        // 实现分页和筛选功能
        Pageable pageable = PageRequest.of(page - 1, size);
        
        // 如果所有筛选条件都为空，返回所有活动
        if ((campus == null || campus.isEmpty() || campus.equals("all")) && 
            (college == null || college.isEmpty() || college.equals("all")) && 
            (type == null || type.isEmpty() || type.equals("all"))) {
            Page<Activity> activityPage = activityRepository.findActivitiesWithFilters(
                null, null, null, "PUBLISHED", pageable);
            return activityPage.getContent();
        }
        
        // 构建筛选条件，使用更灵活的方法
        String campusFilter = (campus != null && !campus.isEmpty() && !campus.equals("all")) ? campus : null;
        String collegeFilter = (college != null && !college.isEmpty() && !college.equals("all")) ? college : null;
        String typeFilter = (type != null && !type.isEmpty() && !type.equals("all")) ? type : null;
        
        // 特殊处理学院筛选：当选择特定学院时，也要包含"全部学院"的活动
        if (collegeFilter != null) {
            // 使用自定义查询来处理"全部学院"的情况
            Page<Activity> activityPage = activityRepository.findActivitiesWithCollegeFilter(
                campusFilter, collegeFilter, typeFilter, "PUBLISHED", pageable);
            return activityPage.getContent();
        }
        
        // 使用模糊匹配的筛选方法
        Page<Activity> activityPage = activityRepository.findActivitiesWithFuzzyFilters(
            campusFilter, collegeFilter, typeFilter, "PUBLISHED", pageable);
        return activityPage.getContent();
    }

    @Override
    public Activity getActivityDetail(Long activityId) {
        return activityRepository.findById(activityId).orElse(null);
    }

    @Override
    public boolean deleteActivity(Long activityId) {
        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (activity == null) return false;
        activity.setStatus("DRAFT");
        activity.setUpdatedAt(LocalDateTime.now());
        activityRepository.save(activity);
        return true;
    }

    @Override
    public boolean applyForActivity(Long activityId, Long userId, String reason) {
        // 简化实现
        return true;
    }

    @Override
    public List<ApplicationDetailDTO> listApplications(Long activityId) {
        // 简化实现
        return new java.util.ArrayList<>();
    }

    @Override
    public boolean handleApplication(Long appId, String action) {
        // 简化实现
        return true;
    }

    @Override
    public boolean likeActivity(Long activityId, Long userId) {
        // 简化实现
        return true;
    }
    
    @Override
    public boolean favoriteActivity(Long activityId, Long userId) {
        // 简化实现
        return true;
    }
    
    @Override
    public List<Activity> getUserCreatedActivities(Long userId) {
        return activityRepository.findByCreatorId(userId);
    }
    
    @Override
    public List<ActivityApplication> getUserApplications(Long userId) {
        // 简化实现
        return new java.util.ArrayList<>();
    }
    
    @Override
    public long getActivityApplicationCount(Long activityId) {
        // 简化实现
        return 0;
    }
    
    @Override
    public long getPendingApplicationCount(Long activityId) {
        // 简化实现
        return 0;
    }
    
    @Override
    public Map<String, Object> getActivityStats(Long activityId) {
        // 简化实现
        return new HashMap<>();
    }
    
    @Override
    public void handleExpiredActivities() {
        // 简化实现
    }
    
    @Override
    public boolean isActivityExpired(Long activityId) {
        // 简化实现
        return false;
    }
}