package com.example.campusmate.service.impl;

import com.example.campusmate.entity.Draft;
import com.example.campusmate.entity.Activity;
import com.example.campusmate.repository.DraftRepository;
import com.example.campusmate.repository.ActivityRepository;
import com.example.campusmate.service.DraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 草稿箱相关业务实现类
 */
@Service
public class DraftServiceImpl implements DraftService {
    @Autowired
    private DraftRepository draftRepository;
    
    @Autowired
    private ActivityRepository activityRepository;

    /**
     * 获取用户草稿列表
     * @param userId 用户ID
     * @param page 页码
     * @return 草稿列表
     */
    @Override
    public List<Draft> listDrafts(Long userId, int page) {
        return draftRepository.findByUserId(userId);
    }

    /**
     * 从草稿箱重新发布
     * @param draftId 草稿ID
     * @return 是否成功
     */
    @Override
    public boolean publishDraft(Long draftId) {
        Draft draft = draftRepository.findById(draftId).orElse(null);
        if (draft == null) {
            return false;
        }
        
        // 获取关联的活动
        Activity activity = activityRepository.findById(draft.getActivityId()).orElse(null);
        if (activity == null) {
            return false;
        }
        
        // 更新活动状态为已发布
        activity.setStatus("PUBLISHED");
        activity.setUpdatedAt(LocalDateTime.now());
        activityRepository.save(activity);
        
        // 删除草稿
        draftRepository.deleteById(draftId);
        
        return true;
    }

    /**
     * 彻底删除草稿
     * @param draftId 草稿ID
     * @return 是否成功
     */
    @Override
    public boolean deleteDraft(Long draftId) {
        Draft draft = draftRepository.findById(draftId).orElse(null);
        if (draft == null) {
            return false;
        }
        
        // 如果草稿有关联的活动，也删除活动
        if (draft.getActivityId() != null) {
            activityRepository.deleteById(draft.getActivityId());
        }
        
        // 删除草稿
        draftRepository.deleteById(draftId);
        return true;
    }
    
    /**
     * 创建草稿（将活动移至草稿箱）
     * @param activityId 活动ID
     * @param userId 用户ID
     * @param reason 原因
     * @return 是否成功
     */
    public boolean createDraft(Long activityId, Long userId, String reason) {
        // 检查是否已存在草稿
        Draft existingDraft = draftRepository.findByActivityId(activityId);
        if (existingDraft != null) {
            return false;
        }
        
        Draft draft = new Draft();
        draft.setUserId(userId);
        draft.setActivityId(activityId);
        draft.setReason(reason);
        draft.setCreatedAt(LocalDateTime.now());
        draftRepository.save(draft);
        
        return true;
    }
    
    /**
     * 获取草稿详情
     * @param draftId 草稿ID
     * @return 草稿信息
     */
    public Draft getDraftDetail(Long draftId) {
        return draftRepository.findById(draftId).orElse(null);
    }
}