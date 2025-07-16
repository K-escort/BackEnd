package com.sw.escort.media.repository;

import com.sw.escort.media.entity.UserInfoPhoto;
import com.sw.escort.user.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInfoPhotoRepository extends JpaRepository<UserInfoPhoto, Long> {
    List<UserInfoPhoto> findByUserInfoId(Long userInfoId);
    List<UserInfoPhoto> findByUserInfo(UserInfo userInfo);
}
