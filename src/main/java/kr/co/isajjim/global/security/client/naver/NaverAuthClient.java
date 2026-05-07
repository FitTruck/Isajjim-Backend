package kr.co.isajjim.global.security.client.naver;

import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import kr.co.isajjim.global.security.dto.NaverUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class NaverAuthClient {

    private static final String USER_INFO_URL = "https://openapi.naver.com/v1/nid/me";

    private final RestClient restClient;

    public NaverUserInfoResponse getUserInfo(String accessToken) {
        return restClient.get()
                .uri(USER_INFO_URL)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new BaseException(ResponseCode.INTERNAL_SERVER_ERROR);
                })
                .body(NaverUserInfoResponse.class);
    }
}
