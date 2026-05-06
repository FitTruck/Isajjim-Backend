package kr.co.isajjim.domains.user.domain.service;

import kr.co.isajjim.domains.user.domain.constant.UserStatus;
import kr.co.isajjim.domains.user.persistence.entity.UserEntity;
import kr.co.isajjim.domains.user.persistence.repository.UserRepository;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import kr.co.isajjim.global.security.constant.SocialProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity getUserById(Long userId) {
        return getOrThrow(userId);
    }

    @Transactional
    public void updateSocialRefreshToken(Long userId, String token) {
        UserEntity user = getOrThrow(userId);
        user.updateSocialRefreshToken(token);
        userRepository.save(user); // Force update to ensure persistence
    }

    public Optional<UserEntity> findBySocialProviderAndSocialId(SocialProvider provider, String socialId) {
        return userRepository.findBySocialProviderAndSocialId(provider, socialId);
    }

    @Transactional
    public UserEntity getOrSaveUser(String name, SocialProvider provider, String socialId, String email, UserStatus status) {
        return userRepository
                .findBySocialProviderAndSocialId(provider, socialId)
                .orElseGet(() -> userRepository.save(
                        UserEntity.socialSignup(
                                name,
                                email,
                                provider,
                                socialId,
                                status
                        )
                ));
    }

    @Transactional
    public void completeSignup(Long userId) {
        UserEntity user = getOrThrow(userId);
        user.completeSignup();
    }

    public void deleteUser(UserEntity user) {
        userRepository.delete(user);
    }

    /* HELPER METHOD */
    private UserEntity getOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BaseException(ResponseCode.NOT_FOUND_USER));
    }
}
