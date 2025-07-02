package com.sw.escort.common.security;


import com.sw.escort.apiPayload.code.exception.GeneralException;
import com.sw.escort.apiPayload.code.status.ErrorStatus;
import com.sw.escort.user.entity.User;
import com.sw.escort.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userPk)  {
        User user = userRepository.findById(Long.parseLong(userPk))
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        return new CustomUserDetail(user);
    }	// 위에서 생성한 CustomUserDetails Class
}

