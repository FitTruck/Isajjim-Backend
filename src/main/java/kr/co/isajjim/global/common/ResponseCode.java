package kr.co.isajjim.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {

    /*    COMMON    */
    // 2xx Success
    OK(HttpStatus.OK, "OK", "요청이 성공적으로 처리되었습니다."),
    CREATED(HttpStatus.CREATED, "CREATED", "리소스가 성공적으로 생성되었습니다."),

    // 400 Bad Request
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON-001", "잘못된 요청입니다."),
    INVALID_METHOD_ARGUMENT(HttpStatus.BAD_REQUEST, "COMMON-002", "올바르지 않은 요청 형식입니다."),
    DATA_INTEGRITY_VIOLATION(HttpStatus.BAD_REQUEST, "COMMON-003", "데이터 무결성 제약 조건을 위반하였습니다."),

    // 403 Forbidden
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON-004", "요청 리소스에 대한 접근 권한이 없습니다."),

    // 404 Not Found
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON-005", "요청 리소스를 찾을 수 없습니다."),

    // 405 Method Not Allowed
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON-006", "요청 메소드를 지원하지 않습니다."),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-007", "서버 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),


    /*    FURNITURE    */
    INVALID_FURNITURE_ESTIMATE_ASSOCIATION(HttpStatus.BAD_REQUEST, "FURNITURE-001", "가구가 해당 견적서에 해당하지 않습니다."),


    /*    ESTIMATE    */
    NOT_FOUND_ESTIMATE(HttpStatus.NOT_FOUND, "ESTIMATE-001", "요청 견적서 ID를 찾을 수 없습니다."),


    /*    Furniture    */
    NOT_FOUND_FURNITURE(HttpStatus.NOT_FOUND, "FURNITURE-001", "요청 가구 ID를 찾을 수 없습니다."),


    /*    Image    */
    NOT_FOUND_IMAGE(HttpStatus.NOT_FOUND, "IMAGE-001", "요청 이미지 ID를 찾을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}