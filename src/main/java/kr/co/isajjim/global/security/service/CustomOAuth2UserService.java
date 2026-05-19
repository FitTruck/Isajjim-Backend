package kr.co.isajjim.global.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.isajjim.domains.user.domain.constant.UserStatus;
import kr.co.isajjim.domains.user.persistence.entity.UserEntity;
import kr.co.isajjim.domains.user.persistence.repository.UserRepository;
import kr.co.isajjim.global.security.auth.CustomUserDetails;
import kr.co.isajjim.global.security.dto.attribute.OAuth2Attributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 로그인 성공 이후 리소스 서버에서 사용자 정보(attributes)를 가져오는 클래스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        Map<String, Object> attributes;
        String userNameAttributeName;

        // Kakao, Google 등은 기존 방식대로 UserInfo Endpoint 호출
        OAuth2User oAuth2User = super.loadUser(userRequest);
        attributes = oAuth2User.getAttributes();
        userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();

        // 유저 정보 생성
        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(registrationId, attributes, userNameAttributeName);

        // 회원가입 및 로그인
        UserEntity user = getOrSaveUser(oAuth2Attributes);

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().getKey())
        );

        // OAuth2User 반환
        return new CustomUserDetails(user.getId(), user.getRole(), authorities, oAuth2Attributes);
    }

    private UserEntity getOrSaveUser(OAuth2Attributes oAuth2Attributes) {
        return userRepository
                .findBySocialProviderAndSocialId(oAuth2Attributes.getProvider(), oAuth2Attributes.getProviderId())
                .orElseGet(() -> userRepository.save(
                        UserEntity.socialSignup(
                                oAuth2Attributes.getName(),
                                oAuth2Attributes.getEmail(),
                                oAuth2Attributes.getProvider(),
                                oAuth2Attributes.getProviderId(),
                                UserStatus.PENDING
                        )
                ));
    }
}