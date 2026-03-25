package kr.co.isajjim.global.security.token;

import kr.co.isajjim.domains.user.domain.constant.Role;
import lombok.Builder;

@Builder
public record TokenClaim(
        Long userId,
        Role role
) {
}