package kr.co.isajjim.domains.user.application.usecase;

import kr.co.isajjim.domains.user.domain.service.UserService;
import kr.co.isajjim.global.annotation.UseCase;
import kr.co.isajjim.infra.s3.domain.service.S3Service;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UserUseCase {

    private final UserService userService;
    private final S3Service s3Service;

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
