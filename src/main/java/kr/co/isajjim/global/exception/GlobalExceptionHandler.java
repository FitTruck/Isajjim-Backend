package kr.co.isajjim.global.exception;

import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException e) {
        ResponseCode responseCode = e.getResponseCode();
        return new ResponseEntity<>(ApiResponse.ofFail(responseCode), responseCode.getStatus());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException: ", e);
        ResponseCode responseCode = ResponseCode.DATA_INTEGRITY_VIOLATION;
        return new ResponseEntity<>(ApiResponse.ofFail(responseCode), responseCode.getStatus());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        log.warn("AuthorizationDeniedException: {}", e.getMessage());
        ResponseCode responseCode = ResponseCode.FORBIDDEN;
        return new ResponseEntity<>(ApiResponse.ofFail(responseCode), responseCode.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Exception: ", e);
        ResponseCode responseCode = ResponseCode.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(ApiResponse.ofFail(responseCode), responseCode.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ResponseCode responseCode = ResponseCode.NOT_FOUND;
        return new ResponseEntity<>(ApiResponse.ofFail(responseCode), status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("HttpRequestMethodNotSupportedException: ", e);
        ResponseCode responseCode = ResponseCode.METHOD_NOT_ALLOWED;
        return new ResponseEntity<>(ApiResponse.ofFail(responseCode), status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("MethodArgumentNotValidException: ", e);
        ResponseCode responseCode = ResponseCode.INVALID_METHOD_ARGUMENT;
        return new ResponseEntity<>(ApiResponse.ofFail(responseCode, e.getBindingResult().getFieldErrors()), status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("JSON Parse Error: ", e);
        ResponseCode responseCode = ResponseCode.BAD_REQUEST;
        return new ResponseEntity<>(ApiResponse.ofFail(responseCode), responseCode.getStatus());
    }
}
