package com.sw.escort.user.entity;

import com.sw.escort.user.dto.req.UserDtoReq;
import com.sw.escort.user.entity.enums.CognitiveStatus;
import com.sw.escort.user.entity.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender; // 성별

    @Enumerated(EnumType.STRING)
    private CognitiveStatus cognitiveStatus; //치매 인지 상태

    private String hometown; //고향
    private String lifeHistory; // 연도별 거주지
    private String familyInfo; //가족 관계
    private String education; // 학교 및 학력
    private String occupation; // 직업 경험
    private String forbiddenKeywords; // 금기어
    private String lifetimeline;// 일대기
    private String lastModifiedBy;// 최종 수정인

    public void update(UserDtoReq.UserInfoUpdateReq req, String modifierName) {
        this.age = req.getAge();
        this.gender = req.getGender();
        this.cognitiveStatus = req.getCognitiveStatus();
        this.hometown = req.getHometown();
        this.lifeHistory = req.getLifeHistory();
        this.familyInfo = req.getFamilyInfo();
        this.education = req.getEducation();
        this.occupation = req.getOccupation();
        this.forbiddenKeywords = req.getForbiddenKeywords();
        this.lifetimeline = req.getLifetimeline();
        this.lastModifiedBy = modifierName;
    }
}