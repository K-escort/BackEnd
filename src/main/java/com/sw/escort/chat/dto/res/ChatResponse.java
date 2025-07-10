package com.sw.escort.chat.dto.res;

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
}

