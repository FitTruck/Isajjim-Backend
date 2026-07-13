package kr.co.isajjim.domains.estimate.presentation.controller;

import kr.co.isajjim.domains.estimate.application.response.AdminEstimateResponse;
import kr.co.isajjim.domains.estimate.application.response.EstimateDetailResponse;
import kr.co.isajjim.domains.estimate.application.usecase.AdminEstimateUseCase;
import kr.co.isajjim.domains.estimate.domain.constant.AIStatus;
import kr.co.isajjim.domains.estimate.presentation.api.AdminEstimateApi;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/estimates")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminEstimateController implements AdminEstimateApi {

    private final AdminEstimateUseCase adminEstimateUseCase;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<Page<AdminEstimateResponse>>> getEstimates(
            @RequestParam(required = false) AIStatus aiStatus,
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        Page<AdminEstimateResponse> response = adminEstimateUseCase.getList(aiStatus, keyword, pageable);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }

    @Override
    @GetMapping("/{estimateId}")
    public ResponseEntity<ApiResponse<EstimateDetailResponse>> getEstimate(
            @PathVariable Long estimateId
    ) {
        EstimateDetailResponse response = adminEstimateUseCase.getDetail(estimateId);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }
}
