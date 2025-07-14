package com.sw.escort.daily.service;

import com.sw.escort.daily.dto.req.DailyDtoReq;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface DailyService {
    void saveDaily(Long userId, DailyDtoReq.RecordDailyReq dailyDTOReq, List<MultipartFile> dailyImages, List<MultipartFile> dailyVideos) throws IOException;
}
