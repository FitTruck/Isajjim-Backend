package kr.co.isajjim.domains.user.presentation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.isajjim.domains.user.application.request.PartnerApprovalRequest;
import kr.co.isajjim.domains.user.application.response.PartnerProfileResponse;
import kr.co.isajjim.domains.user.domain.constant.ApprovalStatus;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Admin Partner", description = "어드민 - 파트너 신청 관리 API")
public interface AdminPartnerApi {

    @Operation(
            summary = "파트너 신청 목록 조회",
            description = "파트너 신청 목록을 승인 상태(status)로 필터링하고, 업체명/대표자명 키워드(keyword)로 검색하여 조회합니다. 둘 다 생략하면 전체 조회합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = PartnerProfileResponse.class,
                    description = "조회 성공"
            ),
            errors = {
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.FORBIDDEN)
            }
    )
    @PageableAsQueryParam
    ResponseEntity<ApiResponse<Page<PartnerProfileResponse>>> getPartnerApplications(
            @Parameter(in = ParameterIn.QUERY, description = "승인 상태 필터", example = "PENDING")
            @RequestParam(required = false) ApprovalStatus status,
            @Parameter(in = ParameterIn.QUERY, description = "업체명/대표자명 검색 키워드", example = "이삿찜")
            @RequestParam(required = false) String keyword,
            @Parameter(hidden = true) Pageable pageable
    );

    @Operation(
            summary = "파트너 신청 승인/거부",
            description = "파트너 신청을 승인하거나 거부합니다. 승인 시 대상 유저의 role이 PARTNER로 변경됩니다. 거부(REJECTED) 시 rejectionReason은 필수입니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = PartnerProfileResponse.class,
                    description = "처리 성공"
            ),
            errors = {
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.FORBIDDEN),
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.NOT_FOUND_PARTNER_PROFILE),
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.INVALID_APPROVAL_STATUS),
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.REQUIRED_REJECTION_REASON)
            }
    )
    ResponseEntity<ApiResponse<PartnerProfileResponse>> decidePartnerApplication(
            @PathVariable Long partnerProfileId,
            @RequestBody @Valid PartnerApprovalRequest request
    );

    @Operation(
            summary = "파트너 신청 삭제",
            description = "파트너 신청 내역을 삭제합니다. 이미 승인(APPROVED)된 파트너인 경우, 삭제와 함께 해당 유저의 role을 USER로 되돌립니다(파트너 권한 해제)."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(description = "삭제 성공"),
            errors = {
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.FORBIDDEN),
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.NOT_FOUND_PARTNER_PROFILE)
            }
    )
    ResponseEntity<ApiResponse<Void>> deletePartnerApplication(
            @PathVariable Long partnerProfileId
    );
}
