package kr.co.isajjim.global.security.dto.attribute;

import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import kr.co.isajjim.global.security.constant.SocialProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public abstract class OAuth2Attributes {

    private final Map<String, Object> attributes;

    private final String userNameAttributeKey;

    abstract public SocialProvider getProvider();

    abstract public String getName();

    abstract public String getEmail();

    public String getProviderId() {
        return String.valueOf(attributes.get(userNameAttributeKey));
    }

    public static OAuth2Attributes of(final String registrationId, final Map<String, Object> attributes,
            final String userNameAttributeKey) {
        if (SocialProvider.GOOGLE.getRegistrationId().equals(registrationId)) {
            return new GoogleOAuth2Attributes(attributes, userNameAttributeKey);
        }

        if (SocialProvider.KAKAO.getRegistrationId().equals(registrationId)) {
            return new KakaoOAuth2Attributes(attributes, "id");
        }
        
        if (SocialProvider.NAVER.getRegistrationId().equals(registrationId)) {
            return new NaverOAuth2Attributes(attributes, "id");
    	}

        throw new BaseException(ResponseCode.NOT_SUPPORTED_SOCIAL_PROVIDER);
    }
}