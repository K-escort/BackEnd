package com.sw.escort.relationship.entity;

import com.sw.escort.relationship.entity.enums.RelationshipType;
import com.sw.escort.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Relationship{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;  // 보호자 등 관계 요청자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;    // 기억지기 (환자 등 대상자)

    @Enumerated(EnumType.STRING)
    private RelationshipType type;
}
