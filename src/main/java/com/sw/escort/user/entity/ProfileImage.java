//package com.sw.escort.user.entity;
//
//import com.sw.escort.global.BaseEntity;
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Builder
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name = "profile_image")
//public class ProfileImage extends BaseEntity {
//
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(unique = true)
//    private String uuid;
//
//    @Column(nullable = false)
//    private String originalFilename;
//
//    @Column
//    private String contentType;
//
//    @Column
//    private Long fileSize;
//
//    @OneToOne(mappedBy = "profileImage")
//    private User user;
//
//}
