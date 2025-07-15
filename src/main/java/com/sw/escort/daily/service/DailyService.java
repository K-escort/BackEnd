package com.sw.escort.daily.service;

import com.sw.escort.daily.dto.req.DailyDtoReq;
import com.sw.escort.daily.dto.res.DailyDtoRes;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public interface DailyService {
    void saveFeedback(Long patientId, String feedback, LocalDate date);
    DailyDtoRes.DailyRes getDaily(Long userId, LocalDate date);
    List<DailyDtoRes.MonthlyRes> getMonthly(Long userId, YearMonth date);
}
