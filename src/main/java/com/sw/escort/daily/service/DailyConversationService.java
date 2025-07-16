package com.sw.escort.daily.service;

import com.sw.escort.daily.dto.res.DailyDtoRes;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface DailyConversationService {
    DailyDtoRes.DailyConversationRes saveConversations(Long userId, LocalDate localDate);
}
