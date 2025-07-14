package com.sw.escort.daily.service;

import com.sw.escort.apiPayload.code.exception.GeneralException;
import com.sw.escort.apiPayload.code.status.ErrorStatus;
import com.sw.escort.common.client.PythonAiClient;
import com.sw.escort.daily.dto.req.DailyDtoReq;
import com.sw.escort.daily.entity.Daily;
import com.sw.escort.daily.repository.DailyRepository;
import com.sw.escort.global.util.AmazonS3Util;
import com.sw.escort.media.entity.UserInfoPhoto;
import com.sw.escort.media.repository.UserInfoPhotoRepository;
import com.sw.escort.user.entity.User;
import com.sw.escort.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyImageServiceImpl implements DailyImageService {

    private final UserRepository userRepository;
    private final UserInfoPhotoRepository photoRepository;
    private final DailyRepository dailyRepository;
    private final AmazonS3Util amazonS3Util;
    private final PythonAiClient pythonAiClient;

    @Override
    public List<String> generateAiImages(DailyDtoReq.DailyImageGenerationReq req, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Daily daily = dailyRepository.findById(req.getDailyId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.DAILY_NOT_FOUND));

        List<UserInfoPhoto> photos = photoRepository.findAllById(req.getPhotoIds());
        if (photos.isEmpty()) {
            throw new GeneralException(ErrorStatus.IMAGE_NOT_FOUND);
        }

        // Python 서버에서 이미지 생성
        List<MultipartFile> generatedImages = pythonAiClient.requestImageGeneration(user, photos);

        // 이미지 업로드 + 메타 저장
        return generatedImages.stream()
                .map(file -> amazonS3Util.uploadDailyImageAndSaveMeta(file, daily))
                .toList();
    }
}
