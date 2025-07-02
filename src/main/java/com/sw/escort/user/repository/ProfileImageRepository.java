package com.sw.escort.user.repository;


import com.sw.escort.user.entity.ProfileImage;
import com.sw.escort.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
    ProfileImage findByUser(User user);
}
