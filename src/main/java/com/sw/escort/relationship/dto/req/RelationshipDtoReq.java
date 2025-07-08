package com.sw.escort.relationship.dto.req;

import com.sw.escort.relationship.entity.enums.RelationshipType;
import com.sw.escort.relationship.entity.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class RelationshipDtoReq {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReq {
        @NotNull
        private Long fromUserId;

        @NotNull
        private Long toUserId;

        @NotNull
        private RelationshipType type;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestReq {
        @NotNull
        private Long receiverId;

        @NotNull
        private RelationshipType type;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RespondReq {
        @NotNull
        private Long requestId;

        @NotNull
        private RequestStatus status;//    PENDING(대기),ACCEPTED(수락),REJECTED(거절)
    }
}
