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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity getUserById(Long userId) {
        return getOrThrow(userId);
    }

    public Map<Long, UserEntity> getUserMapByIds(List<Long> userIds) {
        return userRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(UserEntity::getId, u -> u));
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

    @Transactional
    public void updateName(Long userId, String name) {
        UserEntity user = getOrThrow(userId);
        user.updateName(name);
    }

    @Transactional
    public void updateProfileImage(Long userId, String imageUrl) {
        UserEntity user = getOrThrow(userId);
        user.updateProfileImage(imageUrl);
    }

    @Transactional
    public String deleteProfileImage(Long userId) {
        UserEntity user = getOrThrow(userId);
        String imageUrl = user.getProfileImageUrl();
        user.deleteProfileImage();
        return imageUrl;
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
