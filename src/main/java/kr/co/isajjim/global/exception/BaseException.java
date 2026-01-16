package kr.co.isajjim.global.exception;

import kr.co.isajjim.global.common.BaseResponseCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final BaseResponseCode responseCode;

    public BaseException(BaseResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }
}