package kr.co.isajjim.global.security.component;

import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import kr.co.isajjim.global.security.client.OidcClient;
import kr.co.isajjim.global.security.client.google.GoogleOidcClient;
import kr.co.isajjim.global.security.client.kakao.KakaoOidcClient;
import kr.co.isajjim.global.security.constant.SocialProvider;
import kr.co.isajjim.global.security.dto.OidcPayload;
import kr.co.isajjim.global.security.dto.OidcPublicKey;
import kr.co.isajjim.global.security.dto.OidcPublicKeyResponse;
import kr.co.isajjim.global.security.properties.OidcClientProperties;
import kr.co.isajjim.global.security.properties.google.GoogleOidcProperties;
import kr.co.isajjim.global.security.properties.kakao.KakaoOidcProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class OAuthOidcHelper {
	private final JwtOidcProvider jwtOidcProvider;
	private final Map<SocialProvider, Map<OidcClient, OidcClientProperties>> oauthOidcClients;

    public OAuthOidcHelper(
        JwtOidcProvider jwtOidcProvider,
        KakaoOidcClient kakaoOidcClient,
        GoogleOidcClient googleOidcClient,
        KakaoOidcProperties kakaoOidcProperties,
        GoogleOidcProperties googleOidcProperties
    ) {
        this.jwtOidcProvider = jwtOidcProvider;
        this.oauthOidcClients = Map.of(
                SocialProvider.KAKAO, Map.of(kakaoOidcClient, kakaoOidcProperties),
                SocialProvider.GOOGLE, Map.of(googleOidcClient, googleOidcProperties)
        );
    }

	/**
	 * Provider에 따라 Client와 Properties를 선택하고 Odic public key 정보를 가져와서 ID Token의 payload를 추출하는 메서드
	 *
	 * @param provider : {@link SocialProvider}
	 * @param idToken  : idToken
	 * @return OIDCDecodePayload : ID Token의 payload
	 */
	public OidcPayload getPayload(SocialProvider provider, String idToken) {
		OidcClient client = oauthOidcClients.get(provider).keySet().iterator().next();
		OidcClientProperties properties = oauthOidcClients.get(provider).values().iterator().next();
		OidcPublicKeyResponse response = client.getOidcPublicKey();
		return getPayloadFromIdToken(idToken, properties.getIssuer(), properties.getSecret(), response);
	}

	/**
	 * ID Token의 payload를 추출하는 메서드 <br/>
	 * OAuth 2.0 spec에 따라 ID Token의 유효성 검사 수행 <br/>
	 *
	 * @param idToken  : idToken
	 * @param iss      : ID Token을 발급한 provider의 URL
	 * @param aud      : ID Token이 발급된 앱의 앱 키
	 * @param response : 공개키 목록
	 * @return OidcPayload : ID Token의 payload
	 */
	private OidcPayload getPayloadFromIdToken(String idToken, String iss, String aud, OidcPublicKeyResponse response) {
		String kid = jwtOidcProvider.getKidFromUnsignedTokenHeader(idToken, iss, aud);
		OidcPublicKey key = response.getKeys().stream()
			.filter(k -> k.kid().equals(kid))
			.findFirst()
			.orElseThrow(() -> {
                log.error("일치하는 공개키를 찾을 수 없습니다.");
                return new BaseException(ResponseCode.INTERNAL_SERVER_ERROR);
            });
		return jwtOidcProvider.getOidcTokenBody(idToken, key.n(), key.e());
	}
}
