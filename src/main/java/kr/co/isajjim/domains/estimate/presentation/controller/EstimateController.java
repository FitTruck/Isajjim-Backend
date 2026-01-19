package kr.co.isajjim.domains.estimate.presentation.controller;

import jakarta.validation.Valid;
import kr.co.isajjim.domains.estimate.application.request.EstimateRequest;
import kr.co.isajjim.domains.estimate.application.response.EstimateResponse;
import kr.co.isajjim.domains.estimate.application.usecase.EstimateUseCase;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.CommonResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/estimates")
@RequiredArgsConstructor
public class EstimateController {

    private final EstimateUseCase estimateUseCase;

    @PostMapping()
    public ResponseEntity<ApiResponse<EstimateResponse>> createEstimate(
            @RequestBody @Valid EstimateRequest request
    ) {
        Long savedId = estimateUseCase.createEstimate(request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(CommonResponseCode.OK, EstimateResponse.from(savedId)));
    }
}
