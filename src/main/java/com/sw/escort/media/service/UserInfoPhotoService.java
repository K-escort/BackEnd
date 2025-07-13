package com.sw.escort.media.service;

import com.sw.escort.media.dto.req.UserInfoPhotoDtoReq;
import com.sw.escort.media.dto.res.UserInfoPhotoDtoRes;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserInfoPhotoService {
    String uploadPhoto(UserInfoPhotoDtoReq.Upload request, MultipartFile image, Long modifierId);
    void deletePhoto(Long photoId);
    void updatePhotoInfo(Long photoId, UserInfoPhotoDtoReq.UpdateInfo update, Long modifierId);
    List<UserInfoPhotoDtoRes.Detail> getPhotos(Long userInfoId);
}
