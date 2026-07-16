package kr.co.isajjim.domains.credit.presentation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.isajjim.domains.credit.application.response.CreditBalanceResponse;
import kr.co.isajjim.domains.credit.application.response.CreditTransactionResponse;
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

@Tag(name = "Admin Credit", description = "어드민 - 파트너 크레딧 조회 API")
public interface AdminCreditApi {

    @Operation(
            summary = "파트너 크레딧 잔액 조회",
            description = "CS/분쟁 대응을 위해 관리자가 특정 파트너의 크레딧 잔액을 조회합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = CreditBalanceResponse.class,
                    description = "조회 성공"
            ),
            errors = {
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.FORBIDDEN)
            }
    )
    ResponseEntity<ApiResponse<CreditBalanceResponse>> getBalance(
            @PathVariable Long userId
    );

    @Operation(
            summary = "파트너 크레딧 거래 내역 조회",
            description = "CS/분쟁 대응을 위해 관리자가 특정 파트너의 충전/소모 거래 내역을 최신순으로 조회합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = CreditTransactionResponse.class,
                    description = "조회 성공"
            ),
            errors = {
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.FORBIDDEN)
            }
    )
    @PageableAsQueryParam
    ResponseEntity<ApiResponse<Page<CreditTransactionResponse>>> getHistory(
            @PathVariable Long userId,
            @Parameter(hidden = true) Pageable pageable
    );
}
