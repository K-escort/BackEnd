package com.sw.escort.global.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sw.escort.apiPayload.code.exception.GeneralException;
import com.sw.escort.apiPayload.code.status.ErrorStatus;
import com.sw.escort.daily.entity.Daily;
import com.sw.escort.daily.entity.DailyImage;
import com.sw.escort.daily.entity.DailyVideo;
import com.sw.escort.daily.repository.DailyImageRepository;
import com.sw.escort.daily.repository.DailyRepository;
import com.sw.escort.daily.repository.DailyVideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Util {

    private final AmazonS3 amazonS3Client;
    private final AmazonS3 amazonS3;
    private final DailyImageRepository dailyImageRepository;
    private final DailyVideoRepository dailyVideoRepository;
    private final DailyRepository dailyRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.path.publicData}")
    private String publicDataPath;

    @Value("${cloud.aws.s3.path.dailyImage}")
    private String dailyImagePath;

    @Value("${cloud.aws.s3.path.dailyVideo}")
    private String dailyVideoPath;

    // 최대 파일 용량: 10MB
    private final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    public Optional<String> getPublicDataImageUrlIfExists(String fileName) {
        String key = publicDataPath + "/" + fileName;
        if (!amazonS3.doesObjectExist(bucket, key)) {
            return Optional.empty();
        }
        return Optional.of(amazonS3.getUrl(bucket, key).toString());
    }


    public String uploadImage(MultipartFile file, String folder) {
        validateImage(file); // 용량, 타입 유효성 검사

        // UUID + 파일명 조합으로 고유 키 생성
        String uuid = UUID.randomUUID().toString();
        String key = folder + "/" + uuid + "_" + file.getOriginalFilename();

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            // S3에 객체 업로드
            amazonS3.putObject(new PutObjectRequest(bucket, key, file.getInputStream(), metadata));
        } catch (IOException e) {
            throw new GeneralException(ErrorStatus.FILE_UPLOAD_FAIL);
        }

        // 업로드된 S3 객체의 전체 URL 반환
        return amazonS3.getUrl(bucket, key).toString();
    }

    @Transactional
    public void uploadDailyImages(List<MultipartFile> dailyImages, Daily daily) throws IOException {
        for (MultipartFile multipartFile : dailyImages) {
            String contentType = multipartFile.getContentType();
            if (multipartFile.getSize() > MAX_FILE_SIZE) {
                throw new GeneralException(ErrorStatus.FILE_TOO_LARGE);
            }
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new GeneralException(ErrorStatus.INVALID_FILE_TYPE);
            }

            String uuid = UUID.randomUUID().toString();
            String key = dailyImagePath + "/" + uuid + "_" + multipartFile.getOriginalFilename();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(contentType);
            amazonS3Client.putObject(bucket, key, multipartFile.getInputStream(), metadata);

            DailyImage newdailyImage = DailyImage.builder()
                    .uuid(uuid)
                    .originalFilename(multipartFile.getOriginalFilename())
                    .contentType(contentType)
                    .fileSize(multipartFile.getSize())
                    .daily(daily)
                    .build();

            dailyImageRepository.save(newdailyImage);
        }

        dailyImageRepository.flush();
    }

    @Transactional(readOnly = true)
    public List<String> getDailyImagePath(Long dailyId) {
        Daily daily = dailyRepository.findById(dailyId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.DAILY_NOT_FOUND));

        List<DailyImage> dailyImages = dailyImageRepository.findByDaily(daily);

        return dailyImages.stream()
                .filter(image -> image.getUuid() != null && image.getOriginalFilename() != null)
                .map(image -> amazonS3.getUrl(bucket, dailyImagePath + "/" + image.getUuid() + "_" + image.getOriginalFilename()).toString())
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<String> getDailyVideoPath(Long dailyId) {
        Daily daily = dailyRepository.findById(dailyId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.DAILY_NOT_FOUND));

        List<DailyVideo> dailyVideos = dailyVideoRepository.findByDaily(daily);

        return dailyVideos.stream()
                .filter(image -> image.getUuid() != null && image.getOriginalFilename() != null)
                .map(image -> amazonS3.getUrl(bucket, dailyVideoPath + "/" + image.getUuid() + "_" + image.getOriginalFilename()).toString())
                .collect(Collectors.toList());

    }

    @Transactional
    public void uploadDailyVideos(List<MultipartFile> dailyVideos, Daily daily) throws IOException {
        for (MultipartFile multipartFile : dailyVideos) {
            String contentType = multipartFile.getContentType();
            if (multipartFile.getSize() > MAX_FILE_SIZE) {
                throw new GeneralException(ErrorStatus.FILE_TOO_LARGE);
            }
            if (contentType == null || !contentType.startsWith("video/")) {
                throw new GeneralException(ErrorStatus.INVALID_VIDEO_FILE_TYPE);
            }

            String uuid = UUID.randomUUID().toString();
            String key = dailyVideoPath + "/" + uuid + "_" + multipartFile.getOriginalFilename();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(contentType);
            amazonS3Client.putObject(bucket, key, multipartFile.getInputStream(), metadata);

            DailyVideo newdailyVideo = DailyVideo.builder()
                    .uuid(uuid)
                    .originalFilename(multipartFile.getOriginalFilename())
                    .contentType(contentType)
                    .fileSize(multipartFile.getSize())
                    .daily(daily)
                    .build();

            dailyVideoRepository.save(newdailyVideo);
        }

        dailyImageRepository.flush();
    }

    @Transactional
    public String uploadDailyImageAndSaveMeta(MultipartFile file, Daily daily) {
        validateImage(file);

        String uuid = UUID.randomUUID().toString();
        String key = dailyImagePath + "/" + uuid + "_" + file.getOriginalFilename();

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            amazonS3Client.putObject(bucket, key, file.getInputStream(), metadata);

            DailyImage dailyImage = DailyImage.builder()
                    .uuid(uuid)
                    .originalFilename(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .fileSize(file.getSize())
                    .daily(daily)
                    .build();

            dailyImageRepository.save(dailyImage);
            return amazonS3Client.getUrl(bucket, key).toString();

        } catch (IOException e) {
            throw new GeneralException(ErrorStatus.FILE_UPLOAD_FAIL);
        }
    }


    public void deleteFile(String fileUrlOrKey) {
        try {
            String key = extractKeyFromUrl(fileUrlOrKey); // URL일 경우 키 추출
            amazonS3.deleteObject(bucket, key);
        } catch (Exception e) {
            log.error("S3 파일 삭제 실패: {}", fileUrlOrKey, e);
        }
    }

    private void validateImage(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new GeneralException(ErrorStatus.FILE_TOO_LARGE);
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new GeneralException(ErrorStatus.INVALID_FILE_TYPE);
        }
    }

    // 전체 S3 URL에서 키(path)만 추출
    // fileUrlOrKey 전체 URL 또는 키
    private String extractKeyFromUrl(String fileUrlOrKey) {
        if (fileUrlOrKey.startsWith("https://")) {
            int index = fileUrlOrKey.indexOf(".com/") + 5;
            return fileUrlOrKey.substring(index);
        }
        return fileUrlOrKey;
    }

    // 전체 URL에서 키 경로만 추출
    public String extractFileKeyFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            return url.getPath().substring(1);
        } catch (MalformedURLException e) {
            throw new GeneralException(ErrorStatus.INVALID_IMAGE_URL);
        }
    }

    // 한글 등 인코딩된 S3 Key를 디코딩
    public String decodeKey(String encodedKey) {
        try {
            return URLDecoder.decode(encodedKey, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new GeneralException(ErrorStatus.INVALID_IMAGE_URL);
        }
    }
}
