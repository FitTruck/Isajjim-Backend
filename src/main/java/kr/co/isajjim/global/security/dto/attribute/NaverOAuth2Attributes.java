package kr.co.isajjim.global.security.dto.attribute;

import kr.co.isajjim.global.security.constant.SocialProvider;

import java.util.Map;

public class NaverOAuth2Attributes extends OAuth2Attributes {

    public NaverOAuth2Attributes(final Map<String, Object> attributes, final String userNameAttributeKey) {
        super((Map<String, Object>) attributes.get("response"), userNameAttributeKey);
    }

    @Override
    public SocialProvider getProvider() {
        return SocialProvider.NAVER;
    }

    @Override
    public String getEmail() {
        return String.valueOf(getAttributes().get("email"));
    }

    @Override
    public String getName() {
        return String.valueOf(getAttributes().get("name"));
    }
}