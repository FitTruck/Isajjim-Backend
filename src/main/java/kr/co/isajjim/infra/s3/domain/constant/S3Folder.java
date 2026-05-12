package kr.co.isajjim.infra.s3.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3Folder {

    PROFILE("profile"),
    ROOM("room"),
    CHAT("chat"),
    ;

    private final String path;
}
