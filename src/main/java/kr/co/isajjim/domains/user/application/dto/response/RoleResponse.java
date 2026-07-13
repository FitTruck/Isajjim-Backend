package kr.co.isajjim.domains.user.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.isajjim.domains.user.domain.constant.Role;
import kr.co.isajjim.domains.user.persistence.entity.UserEntity;

public record RoleResponse(
        @Schema(description = "역할", example = "USER")
        Role role
) {
    public static RoleResponse from(UserEntity user) {
        return new RoleResponse(user.getRole());
    }
}
