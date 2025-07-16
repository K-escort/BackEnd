package com.sw.escort.daily.entity;


import com.sw.escort.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "daily_video")
public class DailyVideo extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String uuid;

    @Column(nullable = false)
    private String originalFilename;

    private String contentType;

    private Long fileSize;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_id")
    private Daily daily;
}


