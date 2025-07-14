package com.sw.escort.daily.repository;

import com.sw.escort.daily.entity.DailyImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyImageRepository extends JpaRepository<DailyImage, Long> {
}
