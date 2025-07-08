package com.sw.escort.relationship.converter;

import com.sw.escort.relationship.dto.req.RelationshipDtoReq.*;
import com.sw.escort.relationship.dto.res.RelationshipDtoRes.NotificationRes;
import com.sw.escort.relationship.entity.Relationship;
import com.sw.escort.relationship.entity.RelationshipRequest;
import com.sw.escort.user.entity.User;

public class RelationshipConverter {

    public static Relationship toRelationship(CreateReq req, User fromUser, User toUser) {
        return Relationship.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .type(req.getType())
                .build();
    }

    public static NotificationRes toNotificationRes(RelationshipRequest request) {
        return NotificationRes.builder()
                .requestId(request.getId())
                .senderId(request.getSender().getId())
                .senderName(request.getSender().getName())
                .status(request.getStatus())
                .build();
    }
}
