package kr.co.isajjim.global.security.properties.kakao;

import kr.co.isajjim.global.security.properties.OidcClientProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth2.client.provider.kakao")
public class KakaoOidcProperties implements OidcClientProperties {
	private final String jwksUri;
	private final String secret;
	private final String issuer;
}
