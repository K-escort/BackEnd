package com.sw.escort.daily.repository;

import com.sw.escort.daily.entity.Daily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyRepository extends JpaRepository<Daily,Long> {
    boolean existsByUserIdAndDailyDayRecording(Long userId, LocalDate date);

    Optional<Daily> findByUserIdAndDailyDayRecording(Long userId, LocalDate date);

    List<Daily> findAllByUserIdAndDailyDayRecordingBetweenOrderByDailyDayRecordingAsc(Long userId, LocalDate start, LocalDate end);

    Optional<Daily> findByUserId(Long userId);
}
