package com.sw.escort.daily.repository;

import com.sw.escort.daily.entity.DailyConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyConversationRepository extends JpaRepository<DailyConversation, Long> {
    List<DailyConversation> findByDailyId(Long dailyId);
}
