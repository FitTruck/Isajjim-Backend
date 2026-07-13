package kr.co.isajjim.domains.estimate.presentation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.isajjim.domains.estimate.application.response.AdminEstimateResponse;
import kr.co.isajjim.domains.estimate.application.response.EstimateDetailResponse;
import kr.co.isajjim.domains.estimate.domain.constant.AIStatus;
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
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Admin Estimate", description = "어드민 - 견적서 관리 API")
public interface AdminEstimateApi {

    @Operation(
            summary = "견적서 목록 조회",
            description = "전체 견적서 목록을 AI 처리 상태(aiStatus)로 필터링하고, 신청자 이름/이메일 키워드(keyword)로 검색하여 조회합니다. 둘 다 생략하면 전체 조회합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = AdminEstimateResponse.class,
                    description = "조회 성공"
            ),
            errors = {
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.FORBIDDEN)
            }
    )
    @PageableAsQueryParam
    ResponseEntity<ApiResponse<Page<AdminEstimateResponse>>> getEstimates(
            @Parameter(in = ParameterIn.QUERY, description = "AI 처리 상태 필터", example = "FAILED")
            @RequestParam(required = false) AIStatus aiStatus,
            @Parameter(in = ParameterIn.QUERY, description = "신청자 이름/이메일 검색 키워드", example = "홍길동")
            @RequestParam(required = false) String keyword,
            @Parameter(hidden = true) Pageable pageable
    );

    @Operation(
            summary = "견적서 상세 조회",
            description = "견적서 ID로 상세 정보(위치, 이미지, 견적 항목 포함)를 조회합니다. 소유자 검증 없이 모든 견적서를 조회할 수 있습니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = EstimateDetailResponse.class,
                    description = "조회 성공"
            ),
            errors = {
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.FORBIDDEN),
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.NOT_FOUND_ESTIMATE)
            }
    )
    ResponseEntity<ApiResponse<EstimateDetailResponse>> getEstimate(
            @PathVariable Long estimateId
    );
}
