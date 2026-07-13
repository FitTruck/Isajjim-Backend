package kr.co.isajjim.domains.user.application.usecase;

import kr.co.isajjim.domains.user.application.dto.response.RoleResponse;
import kr.co.isajjim.domains.user.application.dto.response.UserInfoResponse;
import kr.co.isajjim.domains.user.domain.constant.Role;
import kr.co.isajjim.domains.user.domain.service.UserService;
import kr.co.isajjim.domains.user.persistence.entity.UserEntity;
import kr.co.isajjim.global.annotation.UseCase;
import kr.co.isajjim.infra.s3.domain.service.S3Service;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserUseCase {

    private final UserService userService;
    private final S3Service s3Service;

    public UserInfoResponse getMyInfo(Long userId) {
        UserEntity user = userService.getUserById(userId);
        return UserInfoResponse.from(user);
    }

    public RoleResponse getMyRole(Long userId) {
        UserEntity user = userService.getUserById(userId);
        return RoleResponse.from(user);
    }

    @Transactional
    public void updateRole(Long userId, Role role) {
        UserEntity user = userService.getUserById(userId);

        // 어드민이 아닌 유저는 어드민으로 변경할 수 없음
        if (user.getRole() != Role.ADMIN && role == Role.ADMIN) {
            throw new BaseException(ResponseCode.FORBIDDEN);
        }

        userService.updateRole(user, role);
    }

    public void updateName(Long userId, String name) {
        userService.updateName(userId, name);
    }

    public void updateProfileImage(Long userId, String imageUrl) {
        userService.updateProfileImage(userId, imageUrl);
    }

    public void deleteProfileImage(Long userId) {
        String imageUrl = userService.deleteProfileImage(userId);
        if (imageUrl != null) {
            s3Service.deleteObjectByUrl(imageUrl);
        }
    }
}
