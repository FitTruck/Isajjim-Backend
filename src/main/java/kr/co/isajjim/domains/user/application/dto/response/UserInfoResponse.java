package kr.co.isajjim.domains.user.application.dto.response;

import kr.co.isajjim.domains.user.persistence.entity.UserEntity;

public record UserInfoResponse(
        String name,
        String profileImageUrl
) {
    public static UserInfoResponse from(UserEntity user) {
        return new UserInfoResponse(user.getName(), user.getProfileImageUrl());
    }
}
