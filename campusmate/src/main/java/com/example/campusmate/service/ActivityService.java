package com.example.campusmate.service;

import com.example.campusmate.entity.Activity;
import com.example.campusmate.entity.ActivityApplication;
import com.example.campusmate.dto.ApplicationDetailDTO;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Map;

/**
 * 活动管理相关业务接口
 */
public interface ActivityService {
    /**
     * 创建活动
     * @param activity 活动信息
     * @return 创建后的活动
     */
    Activity createActivity(Activity activity);

    /**
     * 获取活动列表（带筛选）
     * @param campus 校区
     * @param college 学院
     * @param type 活动类型
     * @param page 页码
     * @param size 每页数量
     * @return 活动列表
     */
    List<Activity> listActivities(String campus, String college, String type, int page, int size);

    /**
     * 分页获取活动列表（带筛选）
     */
    Page<Activity> listActivitiesWithPage(String campus, String college, String type, int page, int size);

    /**
     * 获取活动详情
     * @param activityId 活动ID
     * @return 活动详情
     */
    Activity getActivityDetail(Long activityId);

    /**
     * 删除活动（移至草稿箱）
     * @param activityId 活动ID
     * @return 是否成功
     */
    boolean deleteActivity(Long activityId);

    /**
     * 申请成为搭子
     * @param activityId 活动ID
     * @param studentId 申请人学号
     * @param reason 申请理由
     * @return 是否成功
     */
    boolean applyForActivity(Long activityId, String studentId, String reason);

    /**
     * 获取活动的申请列表
     * @param activityId 活动ID
     * @return 申请详情列表
     */
    List<ApplicationDetailDTO> listApplications(Long activityId);

    /**
     * 处理申请（同意/拒绝）
     * @param appId 申请ID
     * @param action 操作（ACCEPT/REJECT）
     * @return 是否成功
     */
    boolean handleApplication(Long appId, String action);

    /**
     * 点赞活动
     * @param activityId 活动ID
     * @param studentId 学号
     * @return 是否成功
     */
    boolean likeActivity(Long activityId, String studentId);
    
    /**
     * 收藏活动
     * @param activityId 活动ID
     * @param studentId 学号
     * @return 是否成功
     */
    boolean favoriteActivity(Long activityId, String studentId);
    
    /**
     * 获取用户创建的活动列表
     * @param studentId 学号
     * @return 活动列表
     */
    List<Activity> getUserCreatedActivities(String studentId);
    
    /**
     * 获取用户申请的活动列表
     * @param studentId 学号
     * @return 申请列表
     */
    List<ActivityApplication> getUserApplications(String studentId);
    
    /**
     * 获取活动的申请数量
     * @param activityId 活动ID
     * @return 申请数量
     */
    long getActivityApplicationCount(Long activityId);
    
    /**
     * 获取活动的待处理申请数量
     * @param activityId 活动ID
     * @return 待处理申请数量
     */
    long getPendingApplicationCount(Long activityId);
    
    /**
     * 获取活动统计信息
     * @param activityId 活动ID
     * @return 统计信息
     */
    Map<String, Object> getActivityStats(Long activityId);
    
    /**
     * 自动处理过期活动
     */
    void handleExpiredActivities();
    
    /**
     * 检查活动是否已过期
     * @param activityId 活动ID
     * @return 是否过期
     */
    boolean isActivityExpired(Long activityId);
}