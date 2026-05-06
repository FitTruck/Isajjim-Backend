package kr.co.isajjim.domains.user.application.usecase;

import kr.co.isajjim.domains.refreshtoken.domain.service.RefreshTokenService;
import kr.co.isajjim.domains.user.domain.service.UserService;
import kr.co.isajjim.domains.user.persistence.entity.UserEntity;
import kr.co.isajjim.global.annotation.UseCase;
import kr.co.isajjim.global.security.constant.SocialProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class UserWithdrawalUseCase {

    private final UserService userService;
    private final RestTemplate restTemplate;
    private final RefreshTokenService refreshTokenService;

    @Value("${spring.security.oauth2.client.registration.kakao.admin-key:}")
    private String kakaoAdminKey;

    @Transactional
    public void withdraw(Long userId) {
        UserEntity user = userService.getUserById(userId);

        SocialProvider provider = user.getSocialProvider();
        String socialId = user.getSocialId();

        log.info("Processing withdrawal for user: {} (Provider: {})", userId, provider);

        switch (provider) {
            case KAKAO:
                unlinkKakao(socialId);
                break;
            case GOOGLE:
                revokeGoogle(user.getSocialRefreshToken());
                break;
            default:
                log.warn("Unknown provider for withdrawal: {}", provider);
        }

        // 명시적으로 삭제
        refreshTokenService.deleteAllRefreshToken(userId);

        userService.deleteUser(user);
        log.info("User {} deleted.", userId);
    }

    private void unlinkKakao(String socialId) {
        if (kakaoAdminKey == null || kakaoAdminKey.isBlank()) {
            log.error("Kakao Admin Key is not configured. Skipping unlink.");
            return;
        }

        String url = "https://kapi.kakao.com/v1/user/unlink";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + kakaoAdminKey);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("target_id_type", "user_id");
        body.add("target_id", socialId);

        try {
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
            // 응답 본문 무시
            restTemplate.postForLocation(url, entity);
            log.info("Kakao unlink success for socialId: {}", socialId);
        } catch (Exception e) {
            log.error("Failed to unlink Kakao user: {}", socialId, e);
            // 연결 끊기에 실패하더라도 DB 삭제는 진행 (유저의 탈퇴 의도 존중)
        }
    }

    private void revokeGoogle(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            log.warn("Google refresh token not provided. Skipping Google revocation.");
            return;
        }

        try {
            String url = "https://oauth2.googleapis.com/revoke?token=" + refreshToken;
            restTemplate.postForLocation(url, null);
            log.info("Google revoke success.");
        } catch (Exception e) {
            log.error("Failed to revoke Google token", e);
        }
    }
}
