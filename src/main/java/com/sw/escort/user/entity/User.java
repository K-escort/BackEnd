package com.sw.escort.user.entity;

import com.sw.escort.global.BaseEntity;
import com.sw.escort.user.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    private Integer birthYear;

    @Enumerated(EnumType.STRING)
    private Role role; //역할

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_image_id")
    private ProfileImage profileImage;

    // 헬퍼 메서드: ProfileImage 설정
    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
        if (profileImage != null) {
            profileImage.setUser(this); // 양방향 관계 설정
        }
    }

    // 헬퍼 메서드: ProfileImage 제거
    public void clearProfileImage() {
        if (this.profileImage != null) {
            this.profileImage.setUser(null); // 양방향 관계 해제
            this.profileImage = null;
        }
    }
}
