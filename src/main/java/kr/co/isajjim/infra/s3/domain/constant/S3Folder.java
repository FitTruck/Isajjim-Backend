package kr.co.isajjim.infra.s3.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3Folder {

    PROFILE("profile"),
    ROOM("room"),
    CHAT("chat"),
    BUSINESS_REGISTRATION("business-registration"),
    ;

    private final String path;
}
