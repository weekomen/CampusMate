package com.example.campusmate.service.impl;

import com.example.campusmate.entity.UserInfo;
import com.example.campusmate.repository.UserInfoRepository;
import com.example.campusmate.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public UserInfo getUserInfo(Long userId) {
        return userInfoRepository.findByUserId(userId);
    }

    @Override
    public boolean updateUserInfo(Long userId, UserInfo userInfo) {
        UserInfo old = userInfoRepository.findByUserId(userId);
        if (old == null) return false;
        old.setSignature(userInfo.getSignature());
        old.setInterests(userInfo.getInterests());
        old.setSkills(userInfo.getSkills());
        userInfoRepository.save(old);
        return true;
    }

    @Override
    public boolean updateVisibility(Long userId, String visibility) {
        UserInfo old = userInfoRepository.findByUserId(userId);
        if (old == null) return false;
        old.setInfoVisibility(visibility);
        userInfoRepository.save(old);
        return true;
    }
}