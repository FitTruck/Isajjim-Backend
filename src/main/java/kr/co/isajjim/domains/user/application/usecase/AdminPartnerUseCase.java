package kr.co.isajjim.domains.user.application.usecase;

import kr.co.isajjim.domains.user.application.mapper.PartnerProfileMapper;
import kr.co.isajjim.domains.user.application.request.PartnerApprovalRequest;
import kr.co.isajjim.domains.user.application.response.PartnerProfileResponse;
import kr.co.isajjim.domains.user.domain.constant.ApprovalStatus;
import kr.co.isajjim.domains.user.domain.constant.Role;
import kr.co.isajjim.domains.user.domain.event.PartnerApprovalDecidedEvent;
import kr.co.isajjim.domains.user.domain.service.PartnerProfileService;
import kr.co.isajjim.domains.user.domain.service.UserService;
import kr.co.isajjim.domains.user.persistence.entity.PartnerProfile;
import kr.co.isajjim.global.annotation.UseCase;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminPartnerUseCase {

    private final PartnerProfileService partnerProfileService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    public Page<PartnerProfileResponse> getList(ApprovalStatus approvalStatus, Pageable pageable) {
        return partnerProfileService.getList(approvalStatus, pageable)
                .map(PartnerProfileMapper::fromPartnerProfile);
    }

    @Transactional
    public PartnerProfileResponse decide(Long partnerProfileId, PartnerApprovalRequest request) {
        if (request.approvalStatus() == ApprovalStatus.PENDING) {
            throw new BaseException(ResponseCode.INVALID_APPROVAL_STATUS);
        }

        PartnerProfile partnerProfile = partnerProfileService.getById(partnerProfileId);

        if (request.approvalStatus() == ApprovalStatus.APPROVED) {
            partnerProfileService.approve(partnerProfile);
            userService.updateRole(partnerProfile.getUser(), Role.PARTNER);
        } else {
            if (request.rejectionReason() == null || request.rejectionReason().isBlank()) {
                throw new BaseException(ResponseCode.REQUIRED_REJECTION_REASON);
            }
            partnerProfileService.reject(partnerProfile, request.rejectionReason());
        }

        eventPublisher.publishEvent(new PartnerApprovalDecidedEvent(
                partnerProfile.getUser().getId(),
                request.approvalStatus(),
                request.rejectionReason()
        ));

        return PartnerProfileMapper.fromPartnerProfile(partnerProfile);
    }

    @Transactional
    public void delete(Long partnerProfileId) {
        PartnerProfile partnerProfile = partnerProfileService.getById(partnerProfileId);
        if (partnerProfile.getApprovalStatus() == ApprovalStatus.APPROVED) {
            userService.updateRole(partnerProfile.getUser(), Role.USER);
        }
        partnerProfileService.delete(partnerProfile);
    }
}
