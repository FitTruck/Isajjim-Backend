package kr.co.isajjim.global.security.usecase;

import kr.co.isajjim.domains.user.domain.constant.UserStatus;
import kr.co.isajjim.domains.user.domain.service.UserService;
import kr.co.isajjim.domains.user.persistence.entity.UserEntity;
import kr.co.isajjim.global.annotation.UseCase;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import kr.co.isajjim.global.security.client.google.GoogleAuthClient;
import kr.co.isajjim.global.security.client.naver.NaverAuthClient;
import kr.co.isajjim.global.security.component.OAuthOidcHelper;
import kr.co.isajjim.global.security.constant.SocialProvider;
import kr.co.isajjim.global.security.dto.GoogleTokenResponse;
import kr.co.isajjim.global.security.dto.NaverUserInfoResponse;
import kr.co.isajjim.global.security.dto.OidcPayload;
import kr.co.isajjim.global.security.dto.SignUpRequest;
import kr.co.isajjim.global.security.properties.google.GoogleOidcProperties;
import kr.co.isajjim.global.security.token.JwtProvider;
import kr.co.isajjim.global.security.token.Token;
import kr.co.isajjim.global.security.token.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class OAuth2UseCase {

    private final OAuthOidcHelper oauthOidcHelper;
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final GoogleAuthClient googleAuthClient;
    private final GoogleOidcProperties googleOidcProperties;
    private final NaverAuthClient naverAuthClient;

    public TokenResponse signUp(SocialProvider provider, SignUpRequest.Oidc request) {
        String socialId;
        String email;
        String name;

        if (provider == SocialProvider.NAVER) {
            NaverUserInfoResponse userInfo = naverAuthClient.getUserInfo(request.idToken());
            NaverUserInfoResponse.Response naverResponse = userInfo.response();
            socialId = naverResponse.id();
            email = naverResponse.email();
            name = naverResponse.name();
        } else {
            OidcPayload payload = oauthOidcHelper.getPayload(provider, request.idToken());
            socialId = payload.sub();
            email = payload.email();
            name = payload.name();
        }

        UserEntity user = userService.findBySocialProviderAndSocialId(provider, socialId)
                .orElseGet(() -> {
                    UserStatus status = request.termsAgreed() ? UserStatus.ACTIVE : UserStatus.PENDING;
                    UserEntity savedUser = userService.getOrSaveUser(name, provider, socialId, email, status);
                    if (status == UserStatus.PENDING) {
                        throw new BaseException(ResponseCode.NEED_REGISTER);
                    }
                    return savedUser;
                });

        if (user.getStatus() == UserStatus.PENDING) {
            if (request.termsAgreed()) {
                userService.completeSignup(user.getId());
            } else {
                throw new BaseException(ResponseCode.NEED_REGISTER);
            }
        }

        if (StringUtils.hasText(request.authCode())) {
            String refreshToken = null;
            try {
                if (provider == SocialProvider.GOOGLE) {
                    String redirectUri = "";
                    GoogleTokenResponse response = googleAuthClient.getToken(
                            googleOidcProperties.getSecret(),
                            googleOidcProperties.getClientSecret(),
                            request.authCode(),
                            "authorization_code",
                            redirectUri);
                    refreshToken = response.refreshToken();
                }

                if (refreshToken != null) {
                    userService.updateSocialRefreshToken(user.getId(), refreshToken);
                }
            } catch (Exception e) {
                log.error("Failed to exchange auth code for refresh token", e);
            }
        }

        Token token = jwtProvider.issueToken(user.getId(), user.getRole());

        return TokenResponse.builder()
                .accessToken(token.accessToken())
                .refreshToken(token.refreshToken())
                .build();
    }

    @Transactional
    public void logOut(Long userId, String refreshToken) {
        jwtProvider.removeRefreshToken(userId, refreshToken);
    }
}
