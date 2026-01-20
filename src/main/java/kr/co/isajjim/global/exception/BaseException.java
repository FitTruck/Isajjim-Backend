package kr.co.isajjim.global.exception;

import kr.co.isajjim.global.common.ResponseCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final ResponseCode responseCode;

    public BaseException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }
}