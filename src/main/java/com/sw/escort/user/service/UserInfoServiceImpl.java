package com.sw.escort.user.service;

import com.sw.escort.apiPayload.code.exception.GeneralException;
import com.sw.escort.apiPayload.code.status.ErrorStatus;
import com.sw.escort.relationship.repository.RelationshipRepository;
import com.sw.escort.user.dto.req.UserDtoReq;
import com.sw.escort.user.dto.res.UserDtoRes;
import com.sw.escort.user.entity.User;
import com.sw.escort.user.entity.UserInfo;
import com.sw.escort.user.repository.UserInfoRepository;
import com.sw.escort.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserInfoServiceImpl implements UserInfoService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final RelationshipRepository relationshipRepository;

    @Override
    public void updateUserInfo(UserDtoReq.UserInfoUpdateReq req, Long targetUserId, Long modifierUserId) {
        // 본인, 보호자(or 다른 관계)만 작성 or 수정 허용
        if (!modifierUserId.equals(targetUserId)) {
            boolean related = relationshipRepository.existsByFromUserIdAndToUserId(modifierUserId, targetUserId);
            if (!related) {
                throw new GeneralException(ErrorStatus.UNAUTHORIZED);
            }
        }

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        User modifier = userRepository.findById(modifierUserId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        UserInfo userInfo = userInfoRepository.findByUserId(targetUserId)
                .orElse(UserInfo.builder().user(targetUser).build());

        userInfo.update(req, modifier.getName());
        userInfoRepository.save(userInfo);
    }

    @Override
    public UserDtoRes.UserInfoRes getUserInfo(Long userId) {
        UserInfo info = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_INFO_NOT_FOUND));
        return UserDtoRes.UserInfoRes.from(info);
    }
}
