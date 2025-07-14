package com.sw.escort.daily.repository;

import com.sw.escort.daily.entity.DailyVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyVideoRepository extends JpaRepository<DailyVideo, Long> {
}
