package kr.co.isajjim.domains.credit.presentation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.isajjim.domains.credit.application.request.AdminCreditRefundRequest;
import kr.co.isajjim.domains.credit.application.response.AdminCreditBalanceOverviewResponse;
import kr.co.isajjim.domains.credit.application.response.AdminCreditRefundResponse;
import kr.co.isajjim.domains.credit.application.response.AdminCreditTransactionResponse;
import kr.co.isajjim.global.annotation.swagger.ApiErrorResponseExplanation;
import kr.co.isajjim.global.annotation.swagger.ApiResponseExplanations;
import kr.co.isajjim.global.annotation.swagger.ApiSuccessResponseExplanation;
import kr.co.isajjim.global.annotation.swagger.PageableAsQueryParam;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Admin Credit Overview", description = "어드민 - 전체 파트너 크레딧 현황/환불 API")
public interface AdminCreditOverviewApi {

    @Operation(
            summary = "전체 파트너 크레딧 잔액 현황 조회",
            description = "승인된 파트너 전체의 현재 크레딧 잔액을 업체명/대표자명 키워드로 검색하여 조회합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = AdminCreditBalanceOverviewResponse.class,
                    description = "조회 성공"
            ),
            errors = {
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.FORBIDDEN)
            }
    )
    @PageableAsQueryParam
    ResponseEntity<ApiResponse<Page<AdminCreditBalanceOverviewResponse>>> getBalanceOverview(
            @Parameter(in = ParameterIn.QUERY, description = "업체명/대표자명 검색 키워드", example = "이삿찜")
            @RequestParam(required = false) String keyword,
            @Parameter(hidden = true) Pageable pageable
    );

    @Operation(
            summary = "전체 파트너 크레딧 거래내역 조회",
            description = "전체 파트너의 충전/소모/환불 거래내역을 업체명/대표자명 키워드로 검색하여 최신순으로 조회합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = AdminCreditTransactionResponse.class,
                    description = "조회 성공"
            ),
            errors = {
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.FORBIDDEN)
            }
    )
    @PageableAsQueryParam
    ResponseEntity<ApiResponse<Page<AdminCreditTransactionResponse>>> getAllTransactions(
            @Parameter(in = ParameterIn.QUERY, description = "업체명/대표자명 검색 키워드", example = "이삿찜")
            @RequestParam(required = false) String keyword,
            @Parameter(hidden = true) Pageable pageable
    );

    @Operation(
            summary = "크레딧 충전 건 환불",
            description = "지정한 충전 건(chargeOrderId)에 대해 TossPayments 결제취소 API를 호출하고, 성공 시 해당 크레딧만큼 잔액을 차감합니다. 전액 환불만 지원하며, 이미 환불되었거나 정상 충전 완료(DONE) 상태가 아닌 건은 환불할 수 없습니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = AdminCreditRefundResponse.class,
                    description = "환불 성공"
            ),
            errors = {
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.FORBIDDEN),
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.NOT_FOUND_CREDIT_CHARGE_ORDER),
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.CREDIT_CHARGE_ORDER_NOT_REFUNDABLE),
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.INSUFFICIENT_CREDIT),
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.TOSS_PAYMENT_CANCEL_FAILED)
            }
    )
    ResponseEntity<ApiResponse<AdminCreditRefundResponse>> refundCharge(
            @PathVariable Long chargeOrderId,
            @RequestBody @Valid AdminCreditRefundRequest request
    );
}
