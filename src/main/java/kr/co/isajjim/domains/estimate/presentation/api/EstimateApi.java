package kr.co.isajjim.domains.estimate.presentation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.isajjim.domains.estimate.application.request.EstimateFurnitureUpdateRequest;
import kr.co.isajjim.domains.estimate.application.request.EstimateRequest;
import kr.co.isajjim.domains.estimate.application.request.EstimateUpdateRequest;
import kr.co.isajjim.domains.estimate.application.response.EstimateDetailResponse;
import kr.co.isajjim.domains.estimate.application.response.EstimateResponse;
import kr.co.isajjim.global.annotation.swagger.ApiErrorResponseExplanation;
import kr.co.isajjim.global.annotation.swagger.ApiResponseExplanations;
import kr.co.isajjim.global.annotation.swagger.ApiSuccessResponseExplanation;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.infra.ai.application.dto.AIAnalysisResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "Estimate", description = "견적서 API")
public interface EstimateApi {

    @Operation(
            summary = "견적서 생성(이미지 업로드)",
            description = "사용자에게 이미지 파일 목록을 받아 Firebase Storage에 업로드한 후, 접근 가능한 이미지 URL 목록을 서버에 전송합니다.<br>" +
                    "전송이 완료되면 견적서 ID인 estimateId가 반환되며, 이를 사용하여 다음 단계에서 견적서 정보를 전송합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = EstimateResponse.class,
                    description = "생성 성공"
            )
    )
    ResponseEntity<ApiResponse<EstimateResponse>> createEstimate(
            @RequestBody @Valid EstimateRequest request
    );

    @Operation(
            summary = "견적서 기본 정보 입력",
            description = "이미지 업로드 API에서 응답 받은 estimateId를 사용하여 견적서를 업데이트합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    //todo 추후 삭제
                    responseClass = EstimateDetailResponse.class,
                    description = "수정 성공"
            )
    )
    ResponseEntity<ApiResponse<EstimateDetailResponse>> updateDefaultInfo(
            @PathVariable Long estimateId,
            @RequestBody @Valid EstimateUpdateRequest request
    );

    @Operation(
            summary = "견적서 조회 구독(SSE)",
            description = "성공 응답이 오는 경우에 견적서 조회 API를 호출합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = SseEmitter.class,
                    description = "조회 성공"
            )
    )
    SseEmitter getEstimateSSE(
            @PathVariable Long estimateId
    );

    @Operation(
            summary = "견적서 조회",
            description = "견적서 조회 구독 후 COMPLETED 응답이 올 경우 호출합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = EstimateDetailResponse.class,
                    description = "조회 성공"
            )
    )
    ResponseEntity<ApiResponse<EstimateDetailResponse>> getEstimate(
            @PathVariable Long estimateId
    );


    @Operation(
            summary = "가구 수량 조정",
            description = "‘견적서 기본 정보 입력’에서 응답받은 가구 목록을 사용자가 수정합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "수정 성공"
            ),
            errors = {
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.INVALID_FURNITURE_ESTIMATE_ASSOCIATION)
            }
    )
    @PatchMapping("/{estimateId}/furniture")
    ResponseEntity<ApiResponse<Void>> updateFurniture(
            @PathVariable Long estimateId,
            @RequestBody @Valid EstimateFurnitureUpdateRequest request
    );

    @Operation(
            summary = "AI Callback API"
    )
    ResponseEntity<ApiResponse<Void>> aiCallback(
            @PathVariable Long estimateId,
            @RequestBody AIAnalysisResponse request
    );
}
