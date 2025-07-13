package com.sw.escort.global.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sw.escort.apiPayload.code.exception.GeneralException;
import com.sw.escort.apiPayload.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Util {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 최대 파일 용량: 10MB
    private final long MAX_FILE_SIZE = 10 * 1024 * 1024;

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
