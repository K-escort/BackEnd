package com.sw.escort.daily.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface DailyConversationService {
    Long saveConversations(Long userId, LocalDate localDate);
}
