package com.sw.escort.daily.repository;

import com.sw.escort.daily.entity.Daily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DailyRepository extends JpaRepository<Daily,Long> {
    boolean existsByUserIdAndDailyDayRecording(Long userId, LocalDate date);
}
