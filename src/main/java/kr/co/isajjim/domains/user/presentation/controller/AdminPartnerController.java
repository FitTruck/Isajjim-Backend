package kr.co.isajjim.domains.user.presentation.controller;

import jakarta.validation.Valid;
import kr.co.isajjim.domains.user.application.request.PartnerApprovalRequest;
import kr.co.isajjim.domains.user.application.response.PartnerProfileResponse;
import kr.co.isajjim.domains.user.application.usecase.AdminPartnerUseCase;
import kr.co.isajjim.domains.user.domain.constant.ApprovalStatus;
import kr.co.isajjim.domains.user.presentation.api.AdminPartnerApi;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/partner-applications")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminPartnerController implements AdminPartnerApi {

    private final AdminPartnerUseCase adminPartnerUseCase;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PartnerProfileResponse>>> getPartnerApplications(
            @RequestParam(required = false) ApprovalStatus status,
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        Page<PartnerProfileResponse> response = adminPartnerUseCase.getList(status, keyword, pageable);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }

    @Override
    @PatchMapping("/{partnerProfileId}")
    public ResponseEntity<ApiResponse<PartnerProfileResponse>> decidePartnerApplication(
            @PathVariable Long partnerProfileId,
            @RequestBody @Valid PartnerApprovalRequest request
    ) {
        PartnerProfileResponse response = adminPartnerUseCase.decide(partnerProfileId, request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }

    @Override
    @DeleteMapping("/{partnerProfileId}")
    public ResponseEntity<ApiResponse<Void>> deletePartnerApplication(
            @PathVariable Long partnerProfileId
    ) {
        adminPartnerUseCase.delete(partnerProfileId);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK));
    }
}
