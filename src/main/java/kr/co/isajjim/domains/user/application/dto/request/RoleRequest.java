package kr.co.isajjim.domains.user.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kr.co.isajjim.domains.user.domain.constant.Role;

public record RoleRequest(
        @NotNull
        @Schema(description = "역할", example = "USER")
        Role role
) {
}
