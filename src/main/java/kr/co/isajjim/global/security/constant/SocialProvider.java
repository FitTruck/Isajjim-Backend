package kr.co.isajjim.global.security.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialProvider {

    KAKAO("kakao"),
    NAVER("naver"),
    GOOGLE("google"),
    ;

    private final String registrationId;
}