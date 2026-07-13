package kr.co.isajjim.domains.user.application.mapper;

import kr.co.isajjim.domains.user.application.response.UserResponse;
import kr.co.isajjim.domains.user.persistence.entity.UserEntity;

public class UserMapper {

    public static UserResponse fromUser(UserEntity user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .socialProvider(user.getSocialProvider())
                .status(user.getStatus())
                .profileImageUrl(user.getProfileImageUrl())
                .createdDate(user.getCreatedDate())
                .build();
    }
}
