package com.sw.escort.media.service;

import com.sw.escort.global.util.AmazonS3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicDataPhotoServiceImpl implements PublicDataPhotoService {

    private final AmazonS3Util amazonS3Util;

    @Override
    public List<String> getImageUrls(List<String> imageNames) {
        return imageNames.stream()
                .map(amazonS3Util::getPublicDataImageUrlIfExists)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

}

