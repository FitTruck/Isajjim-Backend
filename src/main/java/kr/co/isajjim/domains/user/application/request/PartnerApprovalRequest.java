package kr.co.isajjim.domains.user.application.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kr.co.isajjim.domains.user.domain.constant.ApprovalStatus;

public record PartnerApprovalRequest(
        @Schema(description = "승인 처리 결과 (APPROVED 또는 REJECTED)", example = "APPROVED")
        @NotNull
        ApprovalStatus approvalStatus,

        @Schema(description = "반려 사유 (approvalStatus가 REJECTED인 경우 필수)", example = "사업자등록증 이미지가 확인되지 않습니다.")
        String rejectionReason
) {
}
