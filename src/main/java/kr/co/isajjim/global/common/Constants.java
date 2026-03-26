package kr.co.isajjim.global.common;

public abstract class Constants {

    public static final String BEARER = "Bearer ";
    public static final String DELIMITER = ":";

    // claim
    public static final String CLAIM_NAME_TOKEN_TYPE = "token";
    public static final String CLAIM_NAME_ROLE = "role";
    public static final String CLAIM_VALUE_ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String CLAIM_VALUE_REFRESH_TOKEN = "REFRESH_TOKEN";

    // header
    public static final String CHARACTER_ENCODING = "UTF-8";

    // whitelist
    public static final String[] WHITELIST = {
            "/api/v1/users/reissue",
            "/login/**",
            "/oauth2/**",
            "/*.css",
            "/error",
            "/favicon.ico",
            "/oauth2/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
    };
}