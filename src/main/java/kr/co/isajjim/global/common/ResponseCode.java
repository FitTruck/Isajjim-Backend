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

    /*    Security    */
    NOT_SUPPORTED_SOCIAL_PROVIDER(HttpStatus.BAD_REQUEST, "AUTH-001", "해당 소셜 로그인은 지원되지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH-002", "인증 정보가 누락되었거나 유효하지 않습니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-003", "토큰이 만료되었습니다."),
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-004", "유효하지 않은 토큰입니다."),
    NEED_REGISTER(HttpStatus.UNAUTHORIZED, "AUTH-005", "회원가입이 필요한 유저입니다.(이용약관 미동의)"),
    INVALID_HEADER(HttpStatus.BAD_REQUEST, "AUTH-006", "헤더가 올바르지 않습니다."),
    INVALID_REDIRECT_URI(HttpStatus.BAD_REQUEST, "AUTH-007", "허용되지 않은 redirect_uri입니다."),

    /*    USER        */
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "USER-001", "요청 유저 ID를 찾을 수 없습니다."),


    /*    ESTIMATE    */
    NOT_FOUND_ESTIMATE(HttpStatus.NOT_FOUND, "ESTIMATE-001", "요청 견적서 ID를 찾을 수 없습니다."),


    /*    Furniture    */
    NOT_FOUND_FURNITURE(HttpStatus.NOT_FOUND, "FURNITURE-001", "요청 가구 ID를 찾을 수 없습니다."),
    INVALID_FURNITURE_ESTIMATE_ASSOCIATION(HttpStatus.BAD_REQUEST, "FURNITURE-002", "가구가 해당 견적서에 해당하지 않습니다."),


    /*    CHAT    */
    NOT_FOUND_CHAT_ROOM(HttpStatus.NOT_FOUND, "CHAT-001", "채팅방을 찾을 수 없습니다."),
    CHAT_ROOM_ACCESS_DENIED(HttpStatus.FORBIDDEN, "CHAT-002", "채팅방에 접근 권한이 없습니다."),

    /*    Image    */
    NOT_FOUND_IMAGE(HttpStatus.NOT_FOUND, "IMAGE-001", "요청 이미지 ID를 찾을 수 없습니다."),


    /*    Partner    */
    NOT_FOUND_PARTNER_PROFILE(HttpStatus.NOT_FOUND, "PARTNER-001", "요청 파트너 신청 정보를 찾을 수 없습니다."),
    DUPLICATE_PARTNER_APPLICATION(HttpStatus.BAD_REQUEST, "PARTNER-002", "이미 파트너 신청 내역이 존재합니다."),
    INVALID_APPROVAL_STATUS(HttpStatus.BAD_REQUEST, "PARTNER-003", "승인 처리 값은 APPROVED 또는 REJECTED만 가능합니다."),
    REQUIRED_REJECTION_REASON(HttpStatus.BAD_REQUEST, "PARTNER-004", "거부 처리 시 반려 사유는 필수입니다."),


    /*    Credit    */
    NOT_FOUND_CREDIT_CHARGE_ORDER(HttpStatus.NOT_FOUND, "CREDIT-001", "요청 크레딧 충전 주문을 찾을 수 없습니다."),
    ALREADY_PROCESSED_CREDIT_CHARGE_ORDER(HttpStatus.CONFLICT, "CREDIT-002", "이미 처리된 크레딧 충전 주문입니다."),
    CREDIT_CHARGE_AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, "CREDIT-003", "충전 요청 금액이 최초 요청 금액과 일치하지 않습니다."),
    INVALID_CREDIT_CHARGE_AMOUNT(HttpStatus.BAD_REQUEST, "CREDIT-004", "충전 금액은 지정된 단위의 배수여야 합니다."),
    TOSS_PAYMENT_CONFIRM_FAILED(HttpStatus.BAD_REQUEST, "CREDIT-005", "결제 승인에 실패했습니다."),
    INSUFFICIENT_CREDIT(HttpStatus.CONFLICT, "CREDIT-006", "보유 크레딧이 부족합니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}