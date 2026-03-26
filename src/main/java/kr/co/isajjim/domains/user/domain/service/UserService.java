package kr.co.isajjim.domains.user.domain.service;

import kr.co.isajjim.domains.user.persistence.entity.UserEntity;
import kr.co.isajjim.domains.user.persistence.repository.UserRepository;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void updateSocialRefreshToken(Long userId, String token) {
        UserEntity user = getOrThrow(userId);
        user.updateSocialRefreshToken(token);
        userRepository.save(user); // Force update to ensure persistence
    }

    /* HELPER METHOD */
    private UserEntity getOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BaseException(ResponseCode.NOT_FOUND_USER));
    }
}
