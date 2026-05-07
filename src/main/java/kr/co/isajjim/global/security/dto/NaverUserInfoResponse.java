package kr.co.isajjim.global.security.dto;

public record NaverUserInfoResponse(
        Response response
) {

    public record Response(
            String id,
            String email,
            String name
    ) {}
}
