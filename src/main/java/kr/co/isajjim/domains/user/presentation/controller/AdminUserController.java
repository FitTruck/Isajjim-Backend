package kr.co.isajjim.domains.user.presentation.controller;

import kr.co.isajjim.domains.user.application.response.UserResponse;
import kr.co.isajjim.domains.user.application.usecase.AdminUserUseCase;
import kr.co.isajjim.domains.user.domain.constant.Role;
import kr.co.isajjim.domains.user.presentation.api.AdminUserApi;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController implements AdminUserApi {

    private final AdminUserUseCase adminUserUseCase;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(
            @RequestParam(required = false) Role role,
            Pageable pageable
    ) {
        Page<UserResponse> response = adminUserUseCase.getList(role, pageable);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }
}
