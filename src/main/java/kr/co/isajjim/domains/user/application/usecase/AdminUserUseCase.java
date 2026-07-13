package kr.co.isajjim.domains.user.application.usecase;

import kr.co.isajjim.domains.user.application.mapper.UserMapper;
import kr.co.isajjim.domains.user.application.response.UserResponse;
import kr.co.isajjim.domains.user.domain.constant.Role;
import kr.co.isajjim.domains.user.domain.service.UserService;
import kr.co.isajjim.global.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminUserUseCase {

    private final UserService userService;

    public Page<UserResponse> getList(Role role, String keyword, Pageable pageable) {
        return userService.getList(role, keyword, pageable)
                .map(UserMapper::fromUser);
    }
}
