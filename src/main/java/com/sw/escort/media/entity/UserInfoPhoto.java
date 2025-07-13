package com.sw.escort.media.entity;

import com.sw.escort.global.BaseEntity;
import com.sw.escort.user.entity.UserInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoPhoto extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_info_id")
    private UserInfo userInfo;

    private String relationToPatient; //사진의 인물과 환자의 관계

    private String originalFileName;

    private UUID uuid;

    private Long fileSize;

    private String description;

    private String url;

    private String lastModifiedBy; // 최종 수정인

    public void updateInfo(String description, String relationToPatient, String modifierName) {
        this.description = description;
        this.relationToPatient = relationToPatient;
        this.lastModifiedBy = modifierName;
    }
}
