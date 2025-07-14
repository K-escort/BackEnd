package com.sw.escort.daily.entity;

import com.sw.escort.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "daily_conversation")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyConversation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_conversation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_id")
    private Daily daily;

    @Column
    private String speaker;

    @Column
    private String content;
}
