package com.example.campusmate.service;

import com.example.campusmate.entity.UserInfo;

/**
 * 用户信息相关业务接口
 */
public interface UserInfoService {
    /**
     * 根据用户ID获取用户信息
     * @param userId App用户ID
     * @return 用户信息
     */
    UserInfo getUserInfo(Long userId);

    /**
     * 更新用户信息
     * @param userId App用户ID
     * @param userInfo 待更新字段
     * @return 是否成功
     */
    boolean updateUserInfo(Long userId, UserInfo userInfo);

    /**
     * 更新信息可见性设置
     * @param userId App用户ID
     * @param visibility 可见性设置JSON
     * @return 是否成功
     */
    boolean updateVisibility(Long userId, String visibility);
}