package kr.co.isajjim.domains.user.application.mapper;

import kr.co.isajjim.domains.user.application.response.PartnerProfileResponse;
import kr.co.isajjim.domains.user.persistence.entity.PartnerProfile;

public class PartnerProfileMapper {

    public static PartnerProfileResponse fromPartnerProfile(PartnerProfile partnerProfile) {
        return PartnerProfileResponse.builder()
                .id(partnerProfile.getId())
                .userId(partnerProfile.getUser().getId())
                .companyName(partnerProfile.getCompanyName())
                .representativeName(partnerProfile.getRepresentativeName())
                .businessRegistrationNumber(partnerProfile.getBusinessRegistrationNumber())
                .businessAddress(partnerProfile.getBusinessAddress())
                .contactPhone(partnerProfile.getContactPhone())
                .introduction(partnerProfile.getIntroduction())
                .businessRegistrationImageUrl(partnerProfile.getBusinessRegistrationImageUrl())
                .approvalStatus(partnerProfile.getApprovalStatus())
                .rejectionReason(partnerProfile.getRejectionReason())
                .createdDate(partnerProfile.getCreatedDate())
                .build();
    }
}
