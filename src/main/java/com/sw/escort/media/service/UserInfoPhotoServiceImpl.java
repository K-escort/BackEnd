package com.sw.escort.media.service;

import com.sw.escort.apiPayload.code.exception.GeneralException;
import com.sw.escort.apiPayload.code.status.ErrorStatus;
import com.sw.escort.global.util.AmazonS3Util;
import com.sw.escort.media.dto.req.UserInfoPhotoDtoReq;
import com.sw.escort.media.dto.res.UserInfoPhotoDtoRes;
import com.sw.escort.media.entity.UserInfoPhoto;
import com.sw.escort.media.repository.UserInfoPhotoRepository;
import com.sw.escort.user.entity.UserInfo;
import com.sw.escort.user.repository.UserInfoRepository;
import com.sw.escort.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserInfoPhotoServiceImpl implements UserInfoPhotoService {

    private final UserInfoPhotoRepository photoRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;
    private final AmazonS3Util amazonS3Util;

    @Value("${cloud.aws.s3.path.userInfo}")
    private String userInfoPath;

    public String uploadPhoto(UserInfoPhotoDtoReq.Upload request, MultipartFile image, Long modifierId) {
        UserInfo userInfo = userInfoRepository.findById(request.getUserInfoId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_INFO_NOT_FOUND));

        String modifierName = userRepository.findById(modifierId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND))
                .getName();

        String url = amazonS3Util.uploadImage(image, userInfoPath);

        UserInfoPhoto photo = UserInfoPhoto.builder()
                .userInfo(userInfo)
                .relationToPatient(request.getRelationToPatient())
                .originalFileName(image.getOriginalFilename())
                .uuid(UUID.randomUUID())
                .fileSize(image.getSize())
                .description(request.getDescription())
                .url(url)
                .lastModifiedBy(modifierName)
                .build();

        photoRepository.save(photo);
        return url;
    }

    public void deletePhoto(Long photoId) {
        UserInfoPhoto photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.IMAGE_NOT_FOUND));
        amazonS3Util.deleteFile(photo.getUrl());
        photoRepository.delete(photo);
    }

    public void updatePhotoInfo(Long photoId, UserInfoPhotoDtoReq.UpdateInfo update, Long modifierId) {
        UserInfoPhoto photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.IMAGE_NOT_FOUND));

        String modifierName = userRepository.findById(modifierId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND))
                .getName();

        photo.updateInfo(update.getDescription(), update.getRelationToPatient(), modifierName);
    }

    @Transactional(readOnly = true)
    public List<UserInfoPhotoDtoRes.Detail> getPhotos(Long userInfoId) {
        return photoRepository.findByUserInfoId(userInfoId).stream()
                .map(UserInfoPhotoDtoRes.Detail::from)
                .collect(Collectors.toList());
    }
}
