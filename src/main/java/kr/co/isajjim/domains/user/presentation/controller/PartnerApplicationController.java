package kr.co.isajjim.domains.user.presentation.controller;

import jakarta.validation.Valid;
import kr.co.isajjim.domains.user.application.request.PartnerApplicationRequest;
import kr.co.isajjim.domains.user.application.response.PartnerProfileResponse;
import kr.co.isajjim.domains.user.application.usecase.PartnerApplicationUseCase;
import kr.co.isajjim.domains.user.presentation.api.PartnerApplicationApi;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.security.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/partner-applications")
@RequiredArgsConstructor
public class PartnerApplicationController implements PartnerApplicationApi {

    private final PartnerApplicationUseCase partnerApplicationUseCase;

    @Override
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'PARTNER')")
    public ResponseEntity<ApiResponse<PartnerProfileResponse>> getMy(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        PartnerProfileResponse response = partnerApplicationUseCase.getMy(user.getUserId());
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }

    @Override
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<PartnerProfileResponse>> apply(
            @RequestBody @Valid PartnerApplicationRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        PartnerProfileResponse response = partnerApplicationUseCase.apply(user.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }

    @Override
    @PatchMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<PartnerProfileResponse>> update(
            @RequestBody @Valid PartnerApplicationRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        PartnerProfileResponse response = partnerApplicationUseCase.update(user.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }

    @Override
    @DeleteMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Void>> cancel(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        partnerApplicationUseCase.cancel(user.getUserId());
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK));
    }
}
