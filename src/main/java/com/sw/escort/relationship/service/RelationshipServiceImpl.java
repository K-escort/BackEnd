package com.sw.escort.relationship.service;

import com.sw.escort.apiPayload.code.exception.GeneralException;
import com.sw.escort.apiPayload.code.status.ErrorStatus;
import com.sw.escort.relationship.converter.RelationshipConverter;
import com.sw.escort.relationship.dto.req.RelationshipDtoReq.*;
import com.sw.escort.relationship.dto.res.GuardianPatientResponse;
import com.sw.escort.relationship.dto.res.RelationshipDtoRes.*;
import com.sw.escort.relationship.dto.res.UserSearchResponse;
import com.sw.escort.relationship.entity.Relationship;
import com.sw.escort.relationship.entity.RelationshipRequest;
import com.sw.escort.relationship.entity.enums.RequestStatus;
import com.sw.escort.relationship.repository.RelationshipRepository;
import com.sw.escort.relationship.repository.RelationshipRequestRepository;
import com.sw.escort.user.entity.User;
import com.sw.escort.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RelationshipServiceImpl implements RelationshipService {

    private final UserRepository userRepository;
    private final RelationshipRepository relationshipRepository;
    private final RelationshipRequestRepository relationshipRequestRepository;

    @Override
    public void createRelationship(CreateReq req) {
        User fromUser = userRepository.findById(req.getFromUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        User toUser = userRepository.findById(req.getToUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Relationship relationship = RelationshipConverter.toRelationship(req, fromUser, toUser);
        relationshipRepository.save(relationship);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GuardianPatientResponse> getPatients(Long guardianId) {
        return relationshipRepository.findByFromUserId(guardianId).stream()
                .map(rel -> GuardianPatientResponse.builder()
                        .userId(rel.getToUser().getId())
                        .name(rel.getToUser().getName())
                        .email(rel.getToUser().getEmail())
                        .role(rel.getToUser().getRole())
                        .relationshipType(rel.getType())
                        .build())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GuardianPatientResponse> getGuardians(Long patientId) {
        return relationshipRepository.findByToUserId(patientId).stream()
                .map(rel -> GuardianPatientResponse.builder()
                        .userId(rel.getFromUser().getId())
                        .name(rel.getFromUser().getName())
                        .email(rel.getFromUser().getEmail())
                        .role(rel.getFromUser().getRole())
                        .relationshipType(rel.getType())
                        .build())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserSearchResponse findUserByEmail(String email, Long currentUserId) {
        User target = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 자기 자신 검색 방지
        if (target.getId().equals(currentUserId)) {
            throw new GeneralException(ErrorStatus.CANNOT_SEARCH_SELF);
        }

        boolean alreadyRelated = relationshipRepository.existsByFromUserIdAndToUserId(currentUserId, target.getId());

        return UserSearchResponse.of(target, alreadyRelated);
    }


    @Override
    public void sendRequest(RequestReq req, Long senderId) {
        Long receiverId = req.getReceiverId();

        // 자기 자신에게 요청 방지
        if (senderId.equals(receiverId)) {
            throw new GeneralException(ErrorStatus.CANNOT_REQUEST_SELF);
        }

        // 이미 관계가 있는 경우 방지
        boolean alreadyRelated = relationshipRepository.existsByFromUserIdAndToUserId(senderId, receiverId);
        if (alreadyRelated) {
            throw new GeneralException(ErrorStatus.ALREADY_RELATED);
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));


        RelationshipRequest request = RelationshipRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(RequestStatus.PENDING)
                .type(req.getType())
                .build();

        relationshipRequestRepository.save(request);
    }

    @Override
    public void respondToRequest(RespondReq req) {
        RelationshipRequest request = relationshipRequestRepository.findById(req.getRequestId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.REQUEST_NOT_FOUND));

        request.respond(req.getStatus()); // 상태 변경 (ACCEPTED or REJECTED)
        relationshipRequestRepository.save(request);

        // 요청 수락 시 관계 자동 생성
        if (req.getStatus() == RequestStatus.ACCEPTED) {
            Long fromUserId = request.getSender().getId();
            Long toUserId = request.getReceiver().getId();

            boolean alreadyExists = relationshipRepository.existsByFromUserIdAndToUserId(fromUserId, toUserId);
            if (!alreadyExists) {
                Relationship relationship = RelationshipConverter.toRelationship(
                        new CreateReq(fromUserId, toUserId, request.getType()),
                        request.getSender(),
                        request.getReceiver()
                );
                relationshipRepository.save(relationship);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationRes> getNotifications(Long userId) {
        return relationshipRequestRepository.findByReceiverIdAndStatus(userId, RequestStatus.PENDING)
                .stream()
                .map(RelationshipConverter::toNotificationRes)
                .toList();
    }
}
