package kr.co.isajjim.domains.user.application.usecase;

import kr.co.isajjim.domains.user.application.mapper.PartnerProfileMapper;
import kr.co.isajjim.domains.user.application.request.PartnerApplicationRequest;
import kr.co.isajjim.domains.user.application.response.PartnerProfileResponse;
import kr.co.isajjim.domains.user.domain.service.PartnerProfileService;
import kr.co.isajjim.domains.user.domain.service.UserService;
import kr.co.isajjim.domains.user.persistence.entity.PartnerProfile;
import kr.co.isajjim.domains.user.persistence.entity.UserEntity;
import kr.co.isajjim.global.annotation.UseCase;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartnerApplicationUseCase {

    private final PartnerProfileService partnerProfileService;
    private final UserService userService;

    public PartnerProfileResponse getMy(Long userId) {
        PartnerProfile partnerProfile = partnerProfileService.getByUserId(userId);
        return PartnerProfileMapper.fromPartnerProfile(partnerProfile);
    }

    @Transactional
    public PartnerProfileResponse apply(Long userId, PartnerApplicationRequest request) {
        UserEntity user = userService.getUserById(userId);

        if (partnerProfileService.existsByUserId(userId)) {
            throw new BaseException(ResponseCode.DUPLICATE_PARTNER_APPLICATION);
        }

        PartnerProfile partnerProfile = partnerProfileService.create(user, request);
        return PartnerProfileMapper.fromPartnerProfile(partnerProfile);
    }

    @Transactional
    public PartnerProfileResponse update(Long userId, PartnerApplicationRequest request) {
        PartnerProfile partnerProfile = partnerProfileService.getByUserId(userId);
        partnerProfileService.update(partnerProfile, request);

        return PartnerProfileMapper.fromPartnerProfile(partnerProfile);
    }

    @Transactional
    public void cancel(Long userId) {
        PartnerProfile partnerProfile = partnerProfileService.getByUserId(userId);
        partnerProfileService.delete(partnerProfile);
    }
}
