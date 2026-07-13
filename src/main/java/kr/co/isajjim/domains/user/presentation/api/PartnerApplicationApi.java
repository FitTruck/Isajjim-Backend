package kr.co.isajjim.domains.user.presentation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.isajjim.domains.user.application.request.PartnerApplicationRequest;
import kr.co.isajjim.domains.user.application.response.PartnerProfileResponse;
import kr.co.isajjim.global.annotation.swagger.ApiErrorResponseExplanation;
import kr.co.isajjim.global.annotation.swagger.ApiResponseExplanations;
import kr.co.isajjim.global.annotation.swagger.ApiSuccessResponseExplanation;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.security.auth.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Partner Application", description = "파트너(업체) 신청 API")
public interface PartnerApplicationApi {

    @Operation(
            summary = "내 파트너 신청 조회",
            description = "본인의 파트너 신청 현황(승인 상태 등)을 조회합니다. 신청 전이라면 새로 신청하고, 신청 후라면 수정/취소할 수 있습니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = PartnerProfileResponse.class,
                    description = "조회 성공"
            ),
            errors = {
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.FORBIDDEN),
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.NOT_FOUND_PARTNER_PROFILE)
            }
    )
    ResponseEntity<ApiResponse<PartnerProfileResponse>> getMy(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails user
    );

    @Operation(
            summary = "파트너 신청",
            description = "일반 유저가 파트너(업체) 전환을 신청합니다. 관리자 승인 전까지는 role이 변경되지 않습니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = PartnerProfileResponse.class,
                    description = "신청 성공"
            ),
            errors = {
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.FORBIDDEN),
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.DUPLICATE_PARTNER_APPLICATION)
            }
    )
    ResponseEntity<ApiResponse<PartnerProfileResponse>> apply(
            @RequestBody @Valid PartnerApplicationRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails user
    );

    @Operation(
            summary = "파트너 신청 수정",
            description = "본인의 파트너 신청 정보를 수정합니다. 반려된 신청을 수정하는 경우 승인 상태가 다시 PENDING으로 초기화됩니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = PartnerProfileResponse.class,
                    description = "수정 성공"
            ),
            errors = {
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.FORBIDDEN),
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.NOT_FOUND_PARTNER_PROFILE)
            }
    )
    ResponseEntity<ApiResponse<PartnerProfileResponse>> update(
            @RequestBody @Valid PartnerApplicationRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails user
    );

    @Operation(
            summary = "파트너 신청 취소",
            description = "본인의 파트너 신청을 취소(삭제)합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(description = "취소 성공"),
            errors = {
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.FORBIDDEN),
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.NOT_FOUND_PARTNER_PROFILE)
            }
    )
    ResponseEntity<ApiResponse<Void>> cancel(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails user
    );
}
