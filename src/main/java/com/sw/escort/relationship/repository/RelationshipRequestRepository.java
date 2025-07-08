package com.sw.escort.relationship.repository;

import com.sw.escort.relationship.entity.RelationshipRequest;
import com.sw.escort.relationship.entity.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RelationshipRequestRepository extends JpaRepository<RelationshipRequest, Long> {
    List<RelationshipRequest> findByReceiverIdAndStatus(Long receiverId, RequestStatus status);
}
