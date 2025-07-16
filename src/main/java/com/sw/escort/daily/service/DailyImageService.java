package com.sw.escort.daily.service;

import com.sw.escort.daily.dto.req.DailyDtoReq;
import com.sw.escort.daily.dto.res.DailyDtoRes;

import java.util.List;

public interface DailyImageService {
    List<DailyDtoRes.DailyImageUploadRes> generateAiImages(Long dailyId);
}
