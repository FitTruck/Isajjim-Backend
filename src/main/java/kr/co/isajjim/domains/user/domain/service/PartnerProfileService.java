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

import java.util.List;

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

    public Page<PartnerProfile> getList(ApprovalStatus approvalStatus, String keyword, Pageable pageable) {
        return partnerProfileRepository.search(approvalStatus, normalizeKeyword(keyword), pageable);
    }

    // 승인된 파트너 중 업체명/대표자명이 keyword와 일치하는 유저 ID 목록. 크레딧 거래내역을 업체명으로 검색할 때 사용.
    public List<Long> findApprovedUserIdsByKeyword(String keyword) {
        return partnerProfileRepository.findUserIdsByApprovalStatusAndKeyword(ApprovalStatus.APPROVED, keyword);
    }

    private String normalizeKeyword(String keyword) {
        return (keyword == null || keyword.isBlank()) ? null : keyword.trim();
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
    public void reject(PartnerProfile partnerProfile, String rejectionReason) {
        partnerProfile.reject(rejectionReason);
    }

    private PartnerProfile getOrThrow(Long id) {
        return partnerProfileRepository.findById(id)
                .orElseThrow(() -> new BaseException(ResponseCode.NOT_FOUND_PARTNER_PROFILE));
    }
}
