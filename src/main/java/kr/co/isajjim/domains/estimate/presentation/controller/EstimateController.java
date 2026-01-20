package kr.co.isajjim.domains.estimate.presentation.controller;

import jakarta.validation.Valid;
import kr.co.isajjim.domains.estimate.application.request.EstimateRequest;
import kr.co.isajjim.domains.estimate.application.request.EstimateUpdateRequest;
import kr.co.isajjim.domains.estimate.application.response.EstimateDetailResponse;
import kr.co.isajjim.domains.estimate.application.response.EstimateResponse;
import kr.co.isajjim.domains.estimate.application.usecase.EstimateUseCase;
import kr.co.isajjim.domains.estimate.presentation.api.EstimateApi;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/estimates")
@RequiredArgsConstructor
public class EstimateController implements EstimateApi {

    private final EstimateUseCase estimateUseCase;

    @Override
    @PostMapping()
    public ResponseEntity<ApiResponse<EstimateResponse>> createEstimate(
            @RequestBody @Valid EstimateRequest request
    ) {
        Long savedId = estimateUseCase.createEstimate(request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, EstimateResponse.from(savedId)));
    }

    @Override
    @PatchMapping("/{estimateId}")
    public ResponseEntity<ApiResponse<EstimateDetailResponse>> updateDefaultInfo(
            @PathVariable Long estimateId,
            @RequestBody @Valid EstimateUpdateRequest request
    ) {
        estimateUseCase.updateDefaultInfo(estimateId, request);
        EstimateDetailResponse response = estimateUseCase.getDetailEstimates(estimateId);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }
}
