package com.sw.escort.relationship.repository;

import com.sw.escort.relationship.entity.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    List<Relationship> findByFromUserId(Long fromUserId);
    List<Relationship> findByToUserId(Long toUserId);
    boolean existsByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

}
