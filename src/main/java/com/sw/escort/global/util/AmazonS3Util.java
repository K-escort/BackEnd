//package com.sw.escort.global.util;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.AmazonS3Exception;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.sw.escort.apiPayload.code.exception.GeneralException;
//import com.sw.escort.apiPayload.code.status.ErrorStatus;
//import com.sw.escort.daily.entity.Daily;
//import com.sw.escort.daily.entity.DailyImage;
//import com.sw.escort.daily.repository.DailyImageRepository;
//import com.sw.escort.daily.repository.DailyRepository;
//import com.sw.escort.test.entity.Test;
//import com.sw.escort.test.entity.TestImage;
//import com.sw.escort.test.repository.TestImageRepository;
//import com.sw.escort.test.repository.TestRepository;
//import com.sw.escort.user.entity.ProfileImage;
//import com.sw.escort.user.entity.User;
//import com.sw.escort.user.repository.ProfileImageRepository;
//import com.sw.escort.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLDecoder;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class AmazonS3Util {
//
//    private final AmazonS3 amazonS3Client;
//    private final AmazonS3 amazonS3;
//    private final UserRepository userRepository;
//    private final DailyRepository dailyRepository;
//    private final DailyImageRepository dailyImageRepository;
//    private final ProfileImageRepository profileImageRepository;
//    private final TestRepository testRepository;
//    private final TestImageRepository testImageRepository;
//
//    final long MAX_FILE_SIZE = 5 * 1024 * 1024;
//
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;
//
//    @Value("${cloud.aws.s3.path.daily}")
//    private String dailyPath;
//
//    @Value("${cloud.aws.s3.path.test}")
//    private String testPath;
//
//    @Value("${cloud.aws.s3.path.todayMood}")
//    private String todayMoodPath;
//
//    @Value("${cloud.aws.s3.path.profile}")
//    private String profilePath;
//
//    @Value("${cloud.aws.s3.path.feedback}")
//    private String feedbackPath;
//
//    public String uploadFeedbackImage(MultipartFile file) {
//        validateImage(file); // 유효성 검사
//
//        String uuid = UUID.randomUUID().toString();
//        String fileName = feedbackPath+"/" + uuid + "_" + file.getOriginalFilename();
//
//        try {
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentLength(file.getSize());
//            metadata.setContentType(file.getContentType());
//            amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
//        } catch (IOException e) {
//            throw new GeneralException(ErrorStatus.FILE_UPLOAD_FAIL);
//        }
//
//        return amazonS3Client.getUrl(bucket, fileName).toString();
//    }
//
//    @Transactional
//    public void uploadDailyImages(List<MultipartFile> dailyImages, Daily daily) throws IOException {
//        for (MultipartFile multipartFile : dailyImages) {
//            String contentType = multipartFile.getContentType();
//            if (multipartFile.getSize() > MAX_FILE_SIZE) {
//                throw new GeneralException(ErrorStatus.FILE_TOO_LARGE);
//            }
//            if (contentType == null || !contentType.startsWith("image/")) {
//                throw new GeneralException(ErrorStatus.INVALID_FILE_TYPE);
//            }
//
//            String uuid = UUID.randomUUID().toString();
//            String key = dailyPath + "/" + uuid + "_" + multipartFile.getOriginalFilename();
//
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentLength(multipartFile.getSize());
//            metadata.setContentType(contentType);
//            amazonS3Client.putObject(bucket, key, multipartFile.getInputStream(), metadata);
//
//            DailyImage newdailyImage = DailyImage.builder()
//                    .uuid(uuid)
//                    .originalFilename(multipartFile.getOriginalFilename())
//                    .contentType(contentType)
//                    .fileSize(multipartFile.getSize())
//                    .daily(daily)
//                    .build();
//
//            dailyImageRepository.save(newdailyImage);
//        }
//
//        dailyImageRepository.flush();
//    }
//
//    //테스트 기록 사진 업로드
//    @Transactional
//    public void uploadTestImages(List<MultipartFile> testImages, Test test) throws IOException {
//        for (MultipartFile multipartFile : testImages) {
//            String contentType = multipartFile.getContentType();
//            if (multipartFile.getSize() > MAX_FILE_SIZE) {
//                throw new GeneralException(ErrorStatus.FILE_TOO_LARGE);
//            }
//            if (contentType == null || !contentType.startsWith("image/")) {
//                throw new GeneralException(ErrorStatus.INVALID_FILE_TYPE);
//            }
//
//            String uuid = UUID.randomUUID().toString();
//            String key = testPath + "/" + uuid + "_" + multipartFile.getOriginalFilename();
//
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentLength(multipartFile.getSize());
//            metadata.setContentType(contentType);
//            amazonS3Client.putObject(bucket, key, multipartFile.getInputStream(), metadata);
//
//            TestImage newtestImage = TestImage.builder()
//                    .uuid(uuid)
//                    .originalFilename(multipartFile.getOriginalFilename())
//                    .contentType(contentType)
//                    .fileSize(multipartFile.getSize())
//                    .test(test)
//                    .build();
//
//            testImageRepository.save(newtestImage);
//        }
//
//        testImageRepository.flush();
//    }
//
//    //사용자 프로필 사진 업로드
//    @Transactional
//    public void profileImageUpload(MultipartFile multipartFile, Long userId) throws IOException {
//
//        String contentType = multipartFile.getContentType();
//        //용량 5MB이하만 받도록 제한
//        if(multipartFile.getSize() > MAX_FILE_SIZE) {
//            throw new GeneralException(ErrorStatus.FILE_TOO_LARGE);
//        }
//
//        //이미지 파일만 받도록 제한
//        if(contentType == null || !contentType.startsWith("image/") ){
//            throw new GeneralException(ErrorStatus.INVALID_FILE_TYPE);
//        } else {
//
//            User user = userRepository.findById(userId)
//                    .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
//
//            ProfileImage existingProfileImage = profileImageRepository.findByUser(user);
//
//            // 기존 이미지 삭제
//            if (existingProfileImage != null) {
//                user.setProfileImage(null);
//                userRepository.save(user);
//                // uuid가 null이 아닌 경우에만 S3에서 삭제
//                if (existingProfileImage.getUuid() != null) {
//                    String existingKey = profilePath + "/" + existingProfileImage.getUuid() + "_" + existingProfileImage.getOriginalFilename();
//                    amazonS3Client.deleteObject(bucket, existingKey);  // S3에서 삭제
//                }
//                profileImageRepository.delete(existingProfileImage);  // DB에서 삭제
//                profileImageRepository.flush();//즉시 DB에 반영
//            }
//
//
//            // 새 이미지 업로드
//            String uuid = UUID.randomUUID().toString();
//            String key = profilePath + "/" + uuid + "_" + multipartFile.getOriginalFilename();
//
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentLength(multipartFile.getSize());
//            metadata.setContentType(multipartFile.getContentType());
//            // S3에 업로드
//            amazonS3Client.putObject(bucket, key, multipartFile.getInputStream(), metadata);
//
//            // DB에 새 프로필 이미지 정보 저장
//            ProfileImage newProfileImage = ProfileImage.builder()
//                    .uuid(uuid)
//                    .originalFilename(multipartFile.getOriginalFilename())
//                    .contentType(multipartFile.getContentType())
//                    .fileSize(multipartFile.getSize())
//                    .build();
//
//            user.setProfileImage(newProfileImage);
//            userRepository.save(user);
//        }
//    }
//
//    //사용자 프로필 기본 이미지로 설정
//    @Transactional
//    public void setDefaultProfileImage(Long userId){
//        String defaultImageKey = profilePath + "/grimmy_icon.png";
//        ObjectMetadata meta;
//        try {
//            meta = getObjectMetadata(defaultImageKey);
//        } catch (AmazonS3Exception e) {
//            throw new GeneralException(ErrorStatus.IMAGE_METADATA_FETCH_FAILED);
//        }
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
//
//        ProfileImage existingProfileImage = profileImageRepository.findByUser(user);
//
//        // 기존 프로필 이미지 제거
//        user.clearProfileImage();
//        String existingKey = profilePath + "/" + existingProfileImage.getUuid() + "_" + existingProfileImage.getOriginalFilename();
//        amazonS3Client.deleteObject(bucket, existingKey);  // S3에서 삭제
//
//        // 새로운 기본 프로필 이미지 설정
//        ProfileImage defaultProfileImage = ProfileImage.builder()
//                .uuid(null) // 기본 이미지는 UUID 없이 저장
//                .originalFilename("grimmy_icon.png")
//                .contentType(meta.getContentType())
//                .fileSize(meta.getContentLength())
//                .build();
//
//        user.setProfileImage(defaultProfileImage); // 헬퍼 메서드로 양방향 관계 설정
//        userRepository.save(user);
//    }
//
//
//    //데일리 이미지 url 가져오기
//    public List<String> getDailyPath(Long dailyId) {
//        Daily daily = dailyRepository.findById(dailyId)
//                .orElseThrow(() -> new GeneralException(ErrorStatus.DAILY_NOT_FOUND));
//
//        List<DailyImage> dailyImages = dailyImageRepository.findByDaily(daily);
//
//        return dailyImages.stream()
//                .filter(image -> image.getUuid() != null && image.getOriginalFilename() != null)
//                .map(image -> amazonS3.getUrl(bucket, dailyPath + "/" + image.getUuid() + "_" + image.getOriginalFilename()).toString())
//                .collect(Collectors.toList());
//
//    }
//
//    //테스트 이미지 url 가져오기
//    public List<String> getTestPath(Long testId) {
//        Test test = testRepository.findById(testId)
//                .orElseThrow(() -> new GeneralException(ErrorStatus.TEST_NOT_FOUND));
//
//        List<TestImage> testImages = testImageRepository.findByTest(test);
//
//        return testImages.stream()
//                .filter(image -> image.getUuid() != null && image.getOriginalFilename() != null)
//                .map(image -> amazonS3.getUrl(bucket, testPath + "/" + image.getUuid() + "_" + image.getOriginalFilename()).toString())
//                .collect(Collectors.toList());
//
//    }
//
//    public String getTodayMoodPath(Long dailyId) {
//        Daily daily = dailyRepository.findById(dailyId).orElseThrow(() -> new GeneralException(ErrorStatus.DAILY_NOT_FOUND));
//        if(daily.getTodayMood() == null) return null;
//        return amazonS3.getUrl(bucket, todayMoodPath + "/" + daily.getTodayMood() + ".png").toString();
//    }
//
//    // MultipartFile 을 전달받아 File 로 전환한 후 S3에 업로드
//    public String upload(MultipartFile multipartFile, String path, DailyImage uuid) throws IOException {
//        File uploadFile = convert(multipartFile)
//                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
//        try {
//            return upload(uploadFile, path, uuid);
//        } finally {
//            removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)
//        }
//    }
//
//    public String getProfileImageUrl(ProfileImage profileImage) {
//        if (profileImage.getUuid() != null) {
//            return amazonS3Client.getUrl(bucket, profilePath + "/" + profileImage.getUuid() + "_" + profileImage.getOriginalFilename()).toString();
//        }
//        return getDefaultProfileImageUrl();
//    }
//
//    public String getDefaultProfileImageUrl() {
//        return amazonS3Client.getUrl(bucket, profilePath + "/grimmy_icon.png").toString();
//    }
//
//    public ObjectMetadata getObjectMetadata(String key) {
//        return amazonS3Client.getObjectMetadata(bucket, key);
//    }
//
//    private String upload(File uploadFile, String path, DailyImage dailyImage) {
//        String fileName = generateKeyName(path, dailyImage);
//        return putS3(uploadFile, fileName);      // 업로드된 파일의 S3 URL 주소 반환
//    }
//
//    private String putS3(File uploadFile, String fileName) {
//        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile));
//        return amazonS3Client.getUrl(bucket, fileName).toString();
//    }
//
//    private void removeNewFile(File targetFile) {
//        if (targetFile.exists() && !targetFile.delete()) {
//            log.error("파일이 삭제되지 못했습니다: {}", targetFile.getAbsolutePath());
//            throw new RuntimeException("파일 삭제 실패: " + targetFile.getAbsolutePath());
//        }
//    }
//
//    private Optional<File> convert(MultipartFile file) throws IOException {
//        File convertFile = new File(System.getProperty("java.io.tmpdir") + "/" + Objects.requireNonNull(file.getOriginalFilename()));
//        if (convertFile.createNewFile()) {
//            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
//                fos.write(file.getBytes());
//            } catch (IOException e) {
//                log.error("파일 변환 중 오류 발생: {}", e.getMessage());
//                throw e;
//            }
//            return Optional.of(convertFile);
//        }
//        return Optional.empty();
//    }
//
//    public String generateKeyName(String path, DailyImage dailyImage) {
//        return dailyPath + '/' + dailyImage.getUuid();
//    }
//
//    // URL에서 파일 경로만 추출하는 메서드 추가
//    public String extractFileKeyFromUrl(String imageUrl) {
//        try {
//            URL url = new URL(imageUrl);
//            return url.getPath().substring(1); // '/' 제거한 경로 반환
//        } catch (MalformedURLException e) {
//            throw new GeneralException(ErrorStatus.INVALID_IMAGE_URL);
//        }
//    }
//
//
//    public void deleteDailyImages(List<String> imageUrls) {
//        if (imageUrls == null || imageUrls.isEmpty()) {
//            throw new GeneralException(ErrorStatus.INVALID_IMAGE_URL);
//        }
//
//        for (String imageUrl : imageUrls) {
//            try {
//                // URL에서 S3 Key 추출
//                String fileKey = extractFileKeyFromUrl(imageUrl);
//
//                // 한글이 포함된 경우 URL 디코딩 적용
//                fileKey = URLDecoder.decode(fileKey, StandardCharsets.UTF_8.name());
//                System.out.println("🗑 삭제할 S3 파일 경로 (디코딩 적용): " + fileKey);
//
//                // S3에서 삭제
//                amazonS3Client.deleteObject(bucket, fileKey);
//            } catch (UnsupportedEncodingException e) {
//                throw new GeneralException(ErrorStatus.INVALID_IMAGE_URL);
//            }
//        }
//    }
//
//    public void deleteDailyImage(String imageUrl) {
//        if (imageUrl == null || imageUrl.isEmpty()) {
//            throw new GeneralException(ErrorStatus.INVALID_IMAGE_URL);
//        }
//
//        try {
//            // URL에서 S3 Key 추출
//            String fileKey = extractFileKeyFromUrl(imageUrl);
//
//            // 한글이 포함된 경우 URL 디코딩 적용
//            fileKey = URLDecoder.decode(fileKey, StandardCharsets.UTF_8.name());
//            System.out.println("🗑 삭제할 S3 파일 경로 (디코딩 적용): " + fileKey);
//
//            // S3에서 삭제
//            amazonS3Client.deleteObject(bucket, fileKey);
//        } catch (UnsupportedEncodingException e) {
//            throw new GeneralException(ErrorStatus.INVALID_IMAGE_URL);
//        }
//    }
//
//    public void deleteTestImage(String imageUrl) {
//        if (imageUrl == null || imageUrl.isEmpty()) {
//            throw new GeneralException(ErrorStatus.INVALID_IMAGE_URL);
//        }
//
//        try {
//            // URL에서 S3 Key 추출
//            String fileKey = extractFileKeyFromUrl(imageUrl);
//
//            // 한글이 포함된 경우 URL 디코딩 적용
//            fileKey = URLDecoder.decode(fileKey, StandardCharsets.UTF_8.name());
//            System.out.println("🗑 삭제할 S3 파일 경로 (디코딩 적용): " + fileKey);
//
//            // S3에서 삭제
//            amazonS3Client.deleteObject(bucket, fileKey);
//        } catch (UnsupportedEncodingException e) {
//            throw new GeneralException(ErrorStatus.INVALID_IMAGE_URL);
//        }
//    }
//
//    // 이미지 유효성 검사
//    private void validateImage(MultipartFile file) {
//        if (file.getSize() > MAX_FILE_SIZE) {
//            throw new GeneralException(ErrorStatus.FILE_TOO_LARGE);
//        }
//        String contentType = file.getContentType();
//        if (contentType == null || !contentType.startsWith("image/")) {
//            throw new GeneralException(ErrorStatus.INVALID_FILE_TYPE);
//        }
//    }
//    // S3 파일 삭제
//    public void deleteFile(String fileUrlOrKey) {
//        try {
//            // S3 키 추출 (URL에서 키 부분만 추출)
//            String key = extractKeyFromUrl(fileUrlOrKey);
//            log.info("S3 파일 삭제 시도: {}", key);
//            amazonS3Client.deleteObject(bucket, key);
//            log.info("S3 파일 삭제 성공: {}", key);
//        } catch (AmazonS3Exception e) {
//            log.error("S3 파일 삭제 실패: {}", fileUrlOrKey, e);
//            // 삭제 실패 시 로그만 남기고 예외를 던지지 않음
//        } catch (Exception e) {
//            log.error("S3 파일 삭제 중 예상치 못한 오류: {}", fileUrlOrKey, e);
//        }
//    }
//
//    // S3 URL 또는 키에서 키 부분만 추출
//    private String extractKeyFromUrl(String fileUrlOrKey) {
//        // S3 URL 형식: https://bucket-name.s3.region.amazonaws.com/path/to/file
//        if (fileUrlOrKey.startsWith("https://")) {
//            int startIndex = fileUrlOrKey.indexOf(".com/") + 5;
//            if (startIndex >= 5 && startIndex < fileUrlOrKey.length()) {
//                return fileUrlOrKey.substring(startIndex);
//            }
//        }
//        // 이미 키 형식이라면 그대로 반환
//        return fileUrlOrKey;
//    }
//
//
//    // 프로필 이미지 키 생성
//    public String getProfileImageKey(ProfileImage profileImage) {
//        return profilePath + "/" + profileImage.getUuid() + "_" + profileImage.getOriginalFilename();
//    }
//
//    // 데일리 이미지 키 생성
//    public String getDailyImageKey(DailyImage dailyImage) {
//        return dailyPath + "/" + dailyImage.getUuid() + "_" + dailyImage.getOriginalFilename();
//    }
//
//}
