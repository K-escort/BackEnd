package com.sw.escort.daily.repository;

import com.sw.escort.daily.entity.Daily;
import com.sw.escort.daily.entity.DailyImage;
import com.sw.escort.daily.entity.DailyVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DailyVideoRepository extends JpaRepository<DailyVideo, Long> {
    Optional<DailyVideo> findByDaily(Daily daily);
}
