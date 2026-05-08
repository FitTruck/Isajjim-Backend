package kr.co.isajjim.domains.chat.application.mapper;

import kr.co.isajjim.domains.chat.application.dto.request.DeviceTokenRequest;
import kr.co.isajjim.domains.chat.persistence.entity.DeviceToken;

public class DeviceTokenMapper {

    public static DeviceToken toDeviceToken(Long userId, DeviceTokenRequest request) {
        return DeviceToken.builder()
                .userId(userId)
                .token(request.token())
                .deviceType(request.deviceType())
                .build();
    }
}
