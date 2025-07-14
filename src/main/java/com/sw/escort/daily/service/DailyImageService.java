package com.sw.escort.daily.service;

import com.sw.escort.daily.dto.req.DailyDtoReq;

import java.util.List;

public interface DailyImageService {
    List<String> generateAiImages(DailyDtoReq.DailyImageGenerationReq req, Long userId);
}
