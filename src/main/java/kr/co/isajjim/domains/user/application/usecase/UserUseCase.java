package kr.co.isajjim.domains.user.application.usecase;

import kr.co.isajjim.domains.user.application.dto.response.UserInfoResponse;
import kr.co.isajjim.domains.user.domain.service.UserService;
import kr.co.isajjim.domains.user.persistence.entity.UserEntity;
import kr.co.isajjim.global.annotation.UseCase;
import kr.co.isajjim.infra.s3.domain.service.S3Service;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UserUseCase {

    private final UserService userService;
    private final S3Service s3Service;

    public UserInfoResponse getMyInfo(Long userId) {
        UserEntity user = userService.getUserById(userId);
        return UserInfoResponse.from(user);
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
