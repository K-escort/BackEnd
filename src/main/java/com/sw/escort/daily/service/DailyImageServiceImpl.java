package com.sw.escort.daily.service;

import com.sw.escort.apiPayload.code.exception.GeneralException;
import com.sw.escort.apiPayload.code.status.ErrorStatus;
import com.sw.escort.common.client.PythonAiClient;
import com.sw.escort.daily.dto.res.DailyDtoRes;
import com.sw.escort.daily.entity.Daily;
import com.sw.escort.daily.entity.DailyImage;
import com.sw.escort.daily.repository.DailyImageRepository;
import com.sw.escort.daily.repository.DailyRepository;
import com.sw.escort.global.util.AmazonS3Util;
import com.sw.escort.media.entity.UserInfoPhoto;
import com.sw.escort.media.repository.UserInfoPhotoRepository;
import com.sw.escort.user.entity.User;
import com.sw.escort.user.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyImageServiceImpl implements DailyImageService {

    private final UserInfoPhotoRepository userInfoPhotoRepository;
    private final DailyRepository dailyRepository;
    private final DailyImageRepository dailyImageRepository;
    private final AmazonS3Util amazonS3Util;
    private final PythonAiClient pythonAiClient;

    @Override
    public List<DailyDtoRes.DailyImageUploadRes> generateAiImages(Long dailyId) {
        Daily daily = dailyRepository.findById(dailyId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.DAILY_NOT_FOUND));

        User user = daily.getUser();
        UserInfo userInfo = user.getUserInfo();

        List<UserInfoPhoto> photos = userInfoPhotoRepository.findByUserInfo(userInfo);
        if (photos.isEmpty()) {
            throw new GeneralException(ErrorStatus.IMAGE_NOT_FOUND);
        }

        // Python 서버에서 이미지 생성
        List<MultipartFile> generatedImages = pythonAiClient.requestImageGeneration(user, photos);

        List<DailyDtoRes.DailyImageUploadRes> responseList = new ArrayList<>();

        for (MultipartFile file : generatedImages) {
            DailyImage saved = amazonS3Util.uploadDailyImageAndSaveMeta(file, daily);

            responseList.add(DailyDtoRes.DailyImageUploadRes.builder()
                    .dailyImageId(saved.getId())
                    .url(saved.getUrl())
                    .build());
        }

        return responseList;
    }
}
