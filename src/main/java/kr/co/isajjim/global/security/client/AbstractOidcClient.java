package kr.co.isajjim.global.security.client;

import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import kr.co.isajjim.global.security.dto.OidcPublicKeyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public abstract class AbstractOidcClient implements OidcClient {

    protected final RestClient restClient;

    protected OidcPublicKeyResponse fetchKey(String url) {
        return restClient.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new BaseException(ResponseCode.INTERNAL_SERVER_ERROR);
                })
                .body(OidcPublicKeyResponse.class);
    }
}