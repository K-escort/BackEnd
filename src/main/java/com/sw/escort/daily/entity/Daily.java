package com.sw.escort.daily.entity;

import com.sw.escort.global.BaseEntity;
import com.sw.escort.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "daily")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Daily extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate dailyDayRecording;

    @Column
    private String feedback;

    @OneToMany(mappedBy = "daily", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<DailyConversation> conversations;

    @OneToMany(mappedBy = "daily",cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<DailyImage> dailyImages;

    @OneToOne(mappedBy = "daily",cascade = CascadeType.REMOVE, orphanRemoval = true)
    private DailyVideo dailyVideo;

    /*
    * //TODO 대화 전체 내용(파이썬 호출로 저장),
    *    AI 생성 이미지,AI생성 동영상,
    *    그 주제에 대해 대화한 LLM에 치료사가 피드백한 내용(피드백은 Role가 HEALER인 사람만 가능)
    * */

}
