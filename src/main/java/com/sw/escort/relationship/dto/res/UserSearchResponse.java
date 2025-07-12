package com.sw.escort.relationship.dto.res;

import com.sw.escort.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserSearchResponse {
    private Long id;
    private String name;
    private String email;
    private boolean alreadyRelated;

    public static UserSearchResponse of(User user, boolean alreadyRelated) {
        return UserSearchResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .alreadyRelated(alreadyRelated)
                .build();
    }
}
