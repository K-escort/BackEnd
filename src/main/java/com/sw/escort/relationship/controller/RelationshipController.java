package com.sw.escort.relationship.controller;

import com.sw.escort.apiPayload.ApiResponse;
import com.sw.escort.apiPayload.code.status.SuccessStatus;
import com.sw.escort.common.security.JwtTokenProvider;
import com.sw.escort.relationship.dto.req.RelationshipDtoReq.*;
import com.sw.escort.relationship.dto.res.GuardianPatientResponse;
import com.sw.escort.relationship.dto.res.RelationshipDtoRes.*;
import com.sw.escort.relationship.dto.res.UserSearchResponse;
import com.sw.escort.relationship.service.RelationshipService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/relationships")
public class RelationshipController {

    private final RelationshipService relationshipService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "테스트 전용 - 관계 직접 생성")
    @PostMapping("/admin/create")
    public ApiResponse<?> createRelationship(@Valid @RequestBody CreateReq req) {
        relationshipService.createRelationship(req);
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    @Operation(summary = "보호자가 등록한 환자 목록 조회")
    @GetMapping("/patients")
    public ApiResponse<List<GuardianPatientResponse>> getPatients() {
        Long guardianId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(relationshipService.getPatients(guardianId));
    }

    @Operation(summary = "환자가 자기 보호자 목록 조회")
    @GetMapping("/guardians")
    public ApiResponse<List<GuardianPatientResponse>> getGuardians() {
        Long patientId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(relationshipService.getGuardians(patientId));
    }

    @Operation(summary = "이메일로 사용자 검색 + 관계 유무 확인")
    @GetMapping("/search")
    public ApiResponse<UserSearchResponse> findUserByEmail(@RequestParam String email) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(relationshipService.findUserByEmail(email,userId));
    }

    @Operation(summary = "관계 요청 전송")
    @PostMapping("/request")
    public ApiResponse<String> sendRequest(@Valid @RequestBody RequestReq req) {
        Long senderId = jwtTokenProvider.getUserIdFromToken();
        relationshipService.sendRequest(req, senderId);
        return ApiResponse.onSuccess("요청 전송 성공!");
    }

    @Operation(summary = "관계 요청 응답 - 수락 하면 자동으로 관계 생성")
    @PostMapping("/respond")
    public ApiResponse<String> respondToRequest(@Valid @RequestBody RespondReq req) {
        relationshipService.respondToRequest(req);
        return ApiResponse.onSuccess("요청 응답 성공!");
    }

    @Operation(summary = "관계 요청 알림 조회")
    @GetMapping("/notifications")
    public ApiResponse<List<NotificationRes>> getNotifications() {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(relationshipService.getNotifications(userId));
    }
}
