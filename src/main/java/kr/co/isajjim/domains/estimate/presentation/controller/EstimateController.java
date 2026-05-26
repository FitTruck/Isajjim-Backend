package kr.co.isajjim.domains.estimate.presentation.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.co.isajjim.domains.estimate.application.request.EstimateFurnitureUpdateRequest;
import kr.co.isajjim.domains.estimate.application.request.EstimateItemUpdateRequest;
import kr.co.isajjim.domains.estimate.application.request.EstimateRequest;
import kr.co.isajjim.domains.estimate.application.request.EstimateUpdateRequest;
import kr.co.isajjim.domains.estimate.application.response.*;
import kr.co.isajjim.domains.estimate.application.usecase.EstimateUseCase;
import kr.co.isajjim.domains.estimate.presentation.api.EstimateApi;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import kr.co.isajjim.global.security.auth.CustomUserDetails;
import kr.co.isajjim.infra.ai.application.dto.AIAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/estimates")
@RequiredArgsConstructor
public class EstimateController implements EstimateApi {

    @Value("${auth.internal-token}")
    private String internalToken;

    private final EstimateUseCase estimateUseCase;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<EstimateResponse>> createEstimate(
            @RequestBody @Valid EstimateRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Long savedId = estimateUseCase.createAndAnalyze(request, user.getUserId());
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, EstimateResponse.from(savedId)));
    }

    @Override
    @PatchMapping("/{estimateId}")
    public ResponseEntity<ApiResponse<EstimateDetailResponse>> updateDefaultInfo(
            @PathVariable Long estimateId,
            @RequestBody @Valid EstimateUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        estimateUseCase.updateDefaultInfo(estimateId, request, user.getUserId());
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK));
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<EstimateDetailListResponse>> getEstimates(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        EstimateDetailListResponse response = estimateUseCase.getEstimates(user.getUserId());
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }

    @Override
    @GetMapping(value = "/{estimateId}/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getEstimateSSE(
            @PathVariable Long estimateId,
            HttpServletResponse response,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        response.setHeader("X-Accel-Buffering", "no");
        return estimateUseCase.subscribe(estimateId, user.getUserId());
    }

    @Override
    @GetMapping("/{estimateId}")
    public ResponseEntity<ApiResponse<EstimateDetailResponse>> getEstimate(
            @PathVariable Long estimateId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        EstimateDetailResponse response = estimateUseCase.getDetailEstimates(estimateId, user.getUserId());
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }

    @Override
    @PatchMapping("/{estimateId}/furniture")
    public ResponseEntity<ApiResponse<Void>> updateFurniture(
            @PathVariable Long estimateId,
            @RequestBody @Valid EstimateFurnitureUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        estimateUseCase.updateFurniture(estimateId, request, user.getUserId());
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK));
    }

    @Override
    @PatchMapping("/{estimateId}/items")
    public ResponseEntity<ApiResponse<Void>> updateItems(
            @PathVariable Long estimateId,
            @RequestBody @Valid EstimateItemUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        estimateUseCase.updateItems(estimateId, request, user.getUserId());
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK));
    }

    @Override
    @PostMapping("/{estimateId}/callback")
    public ResponseEntity<ApiResponse<Void>> aiCallback(
//            @RequestHeader("X_INTERNAL_TOKEN") String token,
            @PathVariable Long estimateId,
            @RequestBody AIAnalysisResponse request
    ) {
//        validateToken(token);
        estimateUseCase.saveFurnitureList(estimateId, request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK));
    }

    @Override
    @PostMapping("/chat-summary")
    public ResponseEntity<ApiResponse<EstimateChatSummaryResponse>> chatSummary(
            @RequestBody String chatContent
    ) {
        EstimateChatSummaryResponse response = estimateUseCase.generateChatSummary(chatContent);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }

    // 추후 Filter 등으로 리팩토링 가능
    private void validateToken(String token) {
        if (!internalToken.equals(token)) {
            throw new BaseException(ResponseCode.UNAUTHORIZED);
        }
    }
}
