package com.sw.escort.daily.repository;

import com.sw.escort.daily.entity.Daily;
import com.sw.escort.daily.entity.DailyImage;
import com.sw.escort.daily.entity.DailyVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyVideoRepository extends JpaRepository<DailyVideo, Long> {
    List<DailyVideo> findByDaily(Daily daily);
}
