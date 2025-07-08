package com.sw.escort.relationship.entity;

import com.sw.escort.global.BaseEntity;
import com.sw.escort.relationship.entity.enums.RelationshipType;
import com.sw.escort.relationship.entity.enums.RequestStatus;
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
public class RelationshipRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender; // 요청 보낸 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver; // 요청 받은 사용자

    @Enumerated(EnumType.STRING)
    private RequestStatus status; // 요청 상태 (PENDING, ACCEPTED, REJECTED)

    @Enumerated(EnumType.STRING)
    private RelationshipType type;

    public void respond(RequestStatus newStatus) {
        if (this.status != RequestStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 요청입니다.");
        }
        this.status = newStatus;
    }
}
