package com.sw.escort.user.service;

import com.sw.escort.user.dto.req.UserDtoReq;
import com.sw.escort.user.dto.res.UserDtoRes;

public interface UserInfoService {
    void updateUserInfo(UserDtoReq.UserInfoUpdateReq req, Long targetUserId, Long modifierUserId);
    UserDtoRes.UserInfoRes getUserInfo(Long userId);
}

