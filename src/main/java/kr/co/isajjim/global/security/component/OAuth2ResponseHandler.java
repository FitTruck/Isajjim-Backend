package kr.co.isajjim.global.security.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.security.properties.OAuth2RedirectProperties;
import kr.co.isajjim.global.utils.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2ResponseHandler {

    private final CookieUtils cookieUtils;
    private final ObjectMapper objectMapper;
    private final OAuth2RedirectProperties oAuth2RedirectProperties;

    public void sendRedirectOrJson(HttpServletRequest request, HttpServletResponse response,
                                   Map<String, String> queryParams, int statusOnJson, Object jsonBody) throws IOException {
        String redirectUri = cookieUtils
                .getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(null);

        if (redirectUri != null) {
            if (!isAllowedRedirectUri(redirectUri)) {
                log.warn("허용되지 않은 redirect_uri 시도: {}", redirectUri);
                writeJson(response, HttpServletResponse.SC_BAD_REQUEST,
                        ApiResponse.ofFail(ResponseCode.INVALID_REDIRECT_URI));
                return;
            }
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(redirectUri);
            queryParams.forEach(builder::queryParam);
            String targetUrl = builder.build().toUriString();

            // 커스텀 스킴 deeplink는 sendRedirect가 아닌 Location 헤더로 직접 처리
            URI targetUri = URI.create(targetUrl);
            String scheme = targetUri.getScheme();
            if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
                response.setStatus(HttpServletResponse.SC_FOUND);
                response.setHeader("Location", targetUrl);
            } else {
                response.sendRedirect(targetUrl);
            }
        } else {
            writeJson(response, statusOnJson, jsonBody);
        }
    }

    private boolean isAllowedRedirectUri(String redirectUri) {
        URI uri = URI.create(redirectUri);
        String scheme = uri.getScheme();

        // 커스텀 스킴 deeplink는 스킴만 비교 (isajjim:// 등)
        if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
            return oAuth2RedirectProperties.allowedRedirectUris().stream()
                    .anyMatch(allowed -> {
                        try {
                            return scheme.equalsIgnoreCase(URI.create(allowed).getScheme());
                        } catch (IllegalArgumentException e) {
                            return false;
                        }
                    });
        }

        // 웹 URI는 origin(scheme+host+port)으로 비교
        String origin = scheme + "://" + uri.getHost()
                + (uri.getPort() == -1 ? "" : ":" + uri.getPort());
        return oAuth2RedirectProperties.allowedRedirectUris().stream()
                .anyMatch(allowedUri -> allowedUri.equalsIgnoreCase(origin));
    }

    private void writeJson(HttpServletResponse response, int status, Object body) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(status);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
