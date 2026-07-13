package kr.co.isajjim.domains.credit.presentation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.isajjim.domains.credit.application.request.CreditChargeConfirmRequest;
import kr.co.isajjim.domains.credit.application.request.CreditChargeReadyRequest;
import kr.co.isajjim.domains.credit.application.response.CreditBalanceResponse;
import kr.co.isajjim.domains.credit.application.response.CreditChargeConfirmResponse;
import kr.co.isajjim.domains.credit.application.response.CreditChargeReadyResponse;
import kr.co.isajjim.domains.credit.application.response.CreditTransactionResponse;
import kr.co.isajjim.global.annotation.swagger.ApiErrorResponseExplanation;
import kr.co.isajjim.global.annotation.swagger.ApiResponseExplanations;
import kr.co.isajjim.global.annotation.swagger.ApiSuccessResponseExplanation;
import kr.co.isajjim.global.annotation.swagger.PageableAsQueryParam;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.security.auth.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Partner Credit", description = "파트너 - 크레딧 충전/조회 API")
public interface PartnerCreditApi {

    @Operation(
            summary = "크레딧 충전 주문 생성",
            description = "TossPayments 결제위젯 호출에 필요한 주문 정보를 생성합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = CreditChargeReadyResponse.class,
                    description = "주문 생성 성공"
            ),
            errors = {
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.INVALID_CREDIT_CHARGE_AMOUNT)
            }
    )
    ResponseEntity<ApiResponse<CreditChargeReadyResponse>> readyCharge(
            @RequestBody @Valid CreditChargeReadyRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails user
    );

    @Operation(
            summary = "크레딧 충전 승인",
            description = "TossPayments 결제 승인을 요청하고, 성공 시 크레딧을 충전합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = CreditChargeConfirmResponse.class,
                    description = "충전 성공"
            ),
            errors = {
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.NOT_FOUND_CREDIT_CHARGE_ORDER),
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.ALREADY_PROCESSED_CREDIT_CHARGE_ORDER),
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.CREDIT_CHARGE_AMOUNT_MISMATCH),
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.TOSS_PAYMENT_CONFIRM_FAILED)
            }
    )
    ResponseEntity<ApiResponse<CreditChargeConfirmResponse>> confirmCharge(
            @RequestBody @Valid CreditChargeConfirmRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails user
    );

    @Operation(
            summary = "내 크레딧 잔액 조회",
            description = "현재 로그인한 파트너의 크레딧 잔액을 조회합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = CreditBalanceResponse.class,
                    description = "조회 성공"
            )
    )
    ResponseEntity<ApiResponse<CreditBalanceResponse>> getBalance(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails user
    );

    @Operation(
            summary = "내 크레딧 거래 내역 조회",
            description = "현재 로그인한 파트너의 충전/소모 거래 내역을 최신순으로 조회합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = CreditTransactionResponse.class,
                    description = "조회 성공"
            )
    )
    @PageableAsQueryParam
    ResponseEntity<ApiResponse<Page<CreditTransactionResponse>>> getHistory(
            @Parameter(hidden = true) Pageable pageable,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails user
    );
}
