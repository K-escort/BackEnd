package com.sw.escort.relationship.service;

import com.sw.escort.relationship.dto.req.RelationshipDtoReq.*;
import com.sw.escort.relationship.dto.res.GuardianPatientResponse;
import com.sw.escort.relationship.dto.res.RelationshipDtoRes.*;
import com.sw.escort.relationship.dto.res.UserSearchResponse;

import java.util.List;

public interface RelationshipService {
    void createRelationship(CreateReq request);
    List<GuardianPatientResponse> getPatients(Long guardianId);
    List<GuardianPatientResponse> getGuardians(Long patientId);
    UserSearchResponse findUserByEmail(String email, Long userId);
    void sendRequest(RequestReq request, Long senderId);
    void respondToRequest(RespondReq request);
    List<NotificationRes> getNotifications(Long userId);
}
