package kr.co.isajjim.domains.estimate.presentation.controller;

import jakarta.validation.Valid;
import kr.co.isajjim.domains.estimate.application.request.*;
import kr.co.isajjim.domains.estimate.application.response.EstimateDetailResponse;
import kr.co.isajjim.domains.estimate.application.response.EstimateResponse;
import kr.co.isajjim.domains.estimate.application.usecase.EstimateUseCase;
import kr.co.isajjim.domains.estimate.presentation.api.EstimateApi;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.infra.ai.application.dto.AIAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/estimates")
@RequiredArgsConstructor
public class EstimateController implements EstimateApi {

    private final EstimateUseCase estimateUseCase;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<EstimateResponse>> createEstimate(
            @RequestBody @Valid EstimateRequest request
    ) {
        Long savedId = estimateUseCase.createAndAnalyze(request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, EstimateResponse.from(savedId)));
    }

    @Override
    @PatchMapping("/{estimateId}")
    public ResponseEntity<ApiResponse<EstimateDetailResponse>> updateDefaultInfo(
            @PathVariable Long estimateId,
            @RequestBody @Valid EstimateUpdateRequest request
    ) {
        estimateUseCase.updateDefaultInfo(estimateId, request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK));
    }

    @Override
    @GetMapping(value = "/{estimateId}/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getEstimateSSE(
            @PathVariable Long estimateId
    ) {
        return estimateUseCase.subscribe(estimateId);
    }

    @Override
    @GetMapping("/{estimateId}")
    public ResponseEntity<ApiResponse<EstimateDetailResponse>> getEstimate(
            @PathVariable Long estimateId
    ) {
        EstimateDetailResponse response = estimateUseCase.getDetailEstimates(estimateId);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }

    @Override
    @PatchMapping("/{estimateId}/furniture")
    public ResponseEntity<ApiResponse<Void>> updateFurniture(
            @PathVariable Long estimateId,
            @RequestBody @Valid EstimateFurnitureUpdateRequest request
    ) {
        estimateUseCase.updateFurniture(estimateId, request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK));
    }

    @Override
    @PatchMapping("/{estimateId}/items")
    public ResponseEntity<ApiResponse<Void>> updateItems(
            @PathVariable Long estimateId,
            @RequestBody @Valid EstimateItemUpdateRequest request
    ) {
        estimateUseCase.updateItems(estimateId, request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK));
    }

    @Override
    @PostMapping("/{estimateId}/callback")
    public ResponseEntity<ApiResponse<Void>> aiCallback(
            @PathVariable Long estimateId,
            @RequestBody AIAnalysisResponse request
    ) {
        estimateUseCase.saveFurnitureList(estimateId, request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK));
    }
}
