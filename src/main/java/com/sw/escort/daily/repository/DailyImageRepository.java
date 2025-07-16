package com.sw.escort.daily.repository;

import com.sw.escort.daily.entity.Daily;
import com.sw.escort.daily.entity.DailyImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DailyImageRepository extends JpaRepository<DailyImage, Long> {
    List<DailyImage> findByDaily(Daily daily);
}
