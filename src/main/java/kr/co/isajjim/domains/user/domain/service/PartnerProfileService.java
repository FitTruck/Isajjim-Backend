package kr.co.isajjim.domains.user.domain.service;

import kr.co.isajjim.domains.user.application.request.PartnerApplicationRequest;
import kr.co.isajjim.domains.user.domain.constant.ApprovalStatus;
import kr.co.isajjim.domains.user.persistence.entity.PartnerProfile;
import kr.co.isajjim.domains.user.persistence.entity.UserEntity;
import kr.co.isajjim.domains.user.persistence.repository.PartnerProfileRepository;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartnerProfileService {

    private final PartnerProfileRepository partnerProfileRepository;

    public PartnerProfile getById(Long partnerProfileId) {
        return getOrThrow(partnerProfileId);
    }

    public PartnerProfile getByUserId(Long userId) {
        return partnerProfileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new BaseException(ResponseCode.NOT_FOUND_PARTNER_PROFILE));
    }

    public boolean existsByUserId(Long userId) {
        return partnerProfileRepository.existsByUser_Id(userId);
    }

    public Page<PartnerProfile> getList(ApprovalStatus approvalStatus, Pageable pageable) {
        if (approvalStatus == null) {
            return partnerProfileRepository.findAll(pageable);
        }
        return partnerProfileRepository.findAllByApprovalStatus(approvalStatus, pageable);
    }

    @Transactional
    public PartnerProfile create(UserEntity user, PartnerApplicationRequest request) {
        PartnerProfile partnerProfile = PartnerProfile.create(
                user,
                request.companyName(),
                request.representativeName(),
                request.businessRegistrationNumber(),
                request.businessAddress(),
                request.contactPhone(),
                request.introduction(),
                request.businessRegistrationImageUrl()
        );
        return partnerProfileRepository.save(partnerProfile);
    }

    @Transactional
    public void update(PartnerProfile partnerProfile, PartnerApplicationRequest request) {
        partnerProfile.updateProfile(
                request.companyName(),
                request.representativeName(),
                request.businessRegistrationNumber(),
                request.businessAddress(),
                request.contactPhone(),
                request.introduction(),
                request.businessRegistrationImageUrl()
        );
    }

    @Transactional
    public void delete(PartnerProfile partnerProfile) {
        partnerProfileRepository.delete(partnerProfile);
    }

    @Transactional
    public void approve(PartnerProfile partnerProfile) {
        partnerProfile.approve();
    }

    @Transactional
    public void reject(PartnerProfile partnerProfile) {
        partnerProfile.reject();
    }

    private PartnerProfile getOrThrow(Long id) {
        return partnerProfileRepository.findById(id)
                .orElseThrow(() -> new BaseException(ResponseCode.NOT_FOUND_PARTNER_PROFILE));
    }
}
