package com.sw.escort.relationship.dto.res;

import com.sw.escort.relationship.entity.RelationshipRequest;
import com.sw.escort.relationship.entity.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RelationshipDtoRes {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NotificationRes {
        private Long requestId; //요청ID
        private Long senderId; //요청 전송자 ID
        private String senderName; //요청 전송자 이름
        private RequestStatus status; //요청 상태
    }
}
