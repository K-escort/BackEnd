package com.sw.escort.daily.converter;

import com.sw.escort.daily.dto.req.DailyDtoReq;
import com.sw.escort.daily.entity.Daily;
import com.sw.escort.global.util.AmazonS3Util;
import com.sw.escort.user.entity.User;
import com.sw.escort.user.entity.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DailyConverter {
    private final AmazonS3Util amazonS3Util;
//    public DailyDTORes.AllDailyPhotoRes toDto(Daily daily) {
//        List<String> imageUrls = amazonS3Util.getDailyPath(daily.getId());
//        String imageUrl = (!imageUrls.isEmpty()) ? imageUrls.get(0) : null;
//        return DailyDTORes.AllDailyPhotoRes.builder()
//                .mode("daily")
//                .createdAt(daily.getCreatedAt())
//                .imageUrl(imageUrl)
//                .build();
//    }

    public Daily toEntity(DailyDtoReq.RecordFeedbackReq dto, User user) {
        return Daily.builder()
                .feedback(dto.getFeedback())
                .build();
    }

}
