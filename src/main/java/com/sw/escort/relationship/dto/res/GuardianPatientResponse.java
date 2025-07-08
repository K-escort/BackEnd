package com.sw.escort.relationship.dto.res;

import com.sw.escort.relationship.entity.enums.RelationshipType;
import com.sw.escort.user.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GuardianPatientResponse {
    private Long userId;
    private String name;
    private String email;
    private Role role;
    private RelationshipType relationshipType;
}
