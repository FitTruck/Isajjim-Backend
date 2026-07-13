package kr.co.isajjim.domains.user.domain.event;

import kr.co.isajjim.domains.user.domain.constant.ApprovalStatus;

public record PartnerApprovalDecidedEvent(
        Long userId,
        ApprovalStatus approvalStatus,
        String rejectionReason
) {}
