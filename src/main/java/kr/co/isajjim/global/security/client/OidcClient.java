package kr.co.isajjim.global.security.client;

import kr.co.isajjim.global.security.dto.OidcPublicKeyResponse;

public interface OidcClient {
	OidcPublicKeyResponse getOidcPublicKey();
}
