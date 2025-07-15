package com.sw.escort.daily.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DailyDtoRes {
    @Data
    @AllArgsConstructor
    @Builder
    public static class DailyRes {
        private Long id;
        private Long userId;
        private LocalDate dailyDayRecording;
        private String feedback;
        private List<String> conversations;
        private List<String> imageUrls;
        private String videoUrl;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class MonthlyRes {
        private Long id;
        private Long userId;
        private LocalDate monthlyDayRecording;
        private String imageUrl;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class ConversationRes {
        private boolean success;
        private int count;
        private List<EachConversationRes> conversations;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class EachConversationRes {
        private String speaker;
        private String content;
        private LocalDateTime timeStamp;
    }
}
