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
import com.example.campusmate.repository.ActivityApplicationRepository;
import com.example.campusmate.repository.ActivityLikeRepository;
import com.example.campusmate.repository.ActivityFavoriteRepository;
import com.example.campusmate.repository.UserInfoRepository;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private ActivityApplicationRepository activityApplicationRepository;
    @Autowired
    private ActivityLikeRepository activityLikeRepository;
    @Autowired
    private ActivityFavoriteRepository activityFavoriteRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

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
        // 检查是否已申请
        if (activityApplicationRepository.existsByActivityIdAndUserId(activityId, userId)) {
            return false;
        }
        ActivityApplication app = new ActivityApplication();
        app.setActivityId(activityId);
        app.setUserId(userId);
        app.setReason(reason);
        app.setStatus("PENDING");
        app.setCreatedAt(LocalDateTime.now());
        app.setUpdatedAt(LocalDateTime.now());
        activityApplicationRepository.save(app);
        return true;
    }

    @Override
    public List<ApplicationDetailDTO> listApplications(Long activityId) {
        List<ActivityApplication> apps = activityApplicationRepository.findByActivityId(activityId);
        List<ApplicationDetailDTO> dtos = new java.util.ArrayList<>();
        Activity activity = activityRepository.findById(activityId).orElse(null);
        for (ActivityApplication app : apps) {
            ApplicationDetailDTO dto = new ApplicationDetailDTO();
            dto.setId(app.getId());
            dto.setActivityId(app.getActivityId());
            dto.setUserId(app.getUserId());
            dto.setReason(app.getReason());
            dto.setStatus(app.getStatus());
            dto.setCreatedAt(app.getCreatedAt());
            dto.setUpdatedAt(app.getUpdatedAt());
            // 用户信息
            var user = userInfoRepository.findByUserId(app.getUserId());
            if (user != null) {
                dto.setStudentId(user.getStudentId());
                dto.setCollege(user.getCollege());
                dto.setCampus(user.getCampus());
                dto.setMajor(user.getMajor());
                dto.setGrade(user.getGrade());
                dto.setSignature(user.getSignature());
                dto.setInterests(user.getInterests());
                dto.setSkills(user.getSkills());
                dto.setAvatarUrl(user.getAvatarUrl());
            }
            // 活动信息
            if (activity != null) {
                dto.setActivityTitle(activity.getTitle());
                dto.setActivityType(activity.getType());
                dto.setActivityTime(activity.getActivityTime());
                dto.setActivityLocation(activity.getLocation());
            }
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public boolean handleApplication(Long appId, String action) {
        ActivityApplication app = activityApplicationRepository.findById(appId).orElse(null);
        if (app == null) return false;
        if ("ACCEPT".equalsIgnoreCase(action)) {
            app.setStatus("ACCEPTED");
        } else if ("REJECT".equalsIgnoreCase(action)) {
            app.setStatus("REJECTED");
        } else {
            return false;
        }
        app.setUpdatedAt(LocalDateTime.now());
        activityApplicationRepository.save(app);
        return true;
    }

    @Override
    public boolean likeActivity(Long activityId, Long userId) {
        // 检查是否已点赞
        if (activityLikeRepository.existsByActivityIdAndUserId(activityId, userId)) {
            return false;
        }
        com.example.campusmate.entity.ActivityLike like = new com.example.campusmate.entity.ActivityLike();
        like.setActivityId(activityId);
        like.setUserId(userId);
        like.setCreatedAt(LocalDateTime.now());
        activityLikeRepository.save(like);
        return true;
    }
    
    @Override
    public boolean favoriteActivity(Long activityId, Long userId) {
        // 检查是否已收藏
        if (activityFavoriteRepository.existsByActivityIdAndUserId(activityId, userId)) {
            return false;
        }
        com.example.campusmate.entity.ActivityFavorite fav = new com.example.campusmate.entity.ActivityFavorite();
        fav.setActivityId(activityId);
        fav.setUserId(userId);
        fav.setCreatedAt(LocalDateTime.now());
        activityFavoriteRepository.save(fav);
        return true;
    }
    
    @Override
    public List<Activity> getUserCreatedActivities(Long userId) {
        return activityRepository.findByCreatorId(userId);
    }
    
    @Override
    public List<ActivityApplication> getUserApplications(Long userId) {
        return activityApplicationRepository.findByUserId(userId);
    }
    
    @Override
    public long getActivityApplicationCount(Long activityId) {
        return activityApplicationRepository.countByActivityId(activityId);
    }
    
    @Override
    public long getPendingApplicationCount(Long activityId) {
        return activityApplicationRepository.countByActivityIdAndStatus(activityId, "PENDING");
    }
    
    @Override
    public Map<String, Object> getActivityStats(Long activityId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("applicationCount", activityApplicationRepository.countByActivityId(activityId));
        stats.put("pendingApplicationCount", activityApplicationRepository.countByActivityIdAndStatus(activityId, "PENDING"));
        stats.put("likeCount", activityLikeRepository.countByActivityId(activityId));
        stats.put("favoriteCount", activityFavoriteRepository.countByActivityId(activityId));
        return stats;
    }
    
    @Override
    public void handleExpiredActivities() {
        // 查找已过期且未标记为EXPIRED的活动
        List<Activity> expired = activityRepository.findExpiredActivities(LocalDateTime.now());
        for (Activity act : expired) {
            if (!"EXPIRED".equals(act.getStatus())) {
                act.setStatus("EXPIRED");
                act.setUpdatedAt(LocalDateTime.now());
                activityRepository.save(act);
            }
        }
    }
    
    @Override
    public boolean isActivityExpired(Long activityId) {
        Activity act = activityRepository.findById(activityId).orElse(null);
        if (act == null) return false;
        return act.getExpireTime() != null && act.getExpireTime().isBefore(LocalDateTime.now());
    }
}