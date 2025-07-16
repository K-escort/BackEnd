package com.sw.escort.chat.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ChatResponse {
    private boolean success;
    private String message;
    private ChatDetail response;

    @Getter
    public static class ChatDetail {
        private String message;
        private String evaluation;
        private String topic;
        private boolean is_ok;
    }

    @Getter
    @AllArgsConstructor
    public static class TopicResponseDto {
        private String topic;
        private String url;
    }
}

