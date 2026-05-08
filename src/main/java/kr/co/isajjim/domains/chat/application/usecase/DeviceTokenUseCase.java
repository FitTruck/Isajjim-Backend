package kr.co.isajjim.domains.chat.application.usecase;

import kr.co.isajjim.domains.chat.application.dto.request.DeviceTokenRequest;
import kr.co.isajjim.domains.chat.application.mapper.DeviceTokenMapper;
import kr.co.isajjim.domains.chat.domain.service.DeviceTokenService;
import kr.co.isajjim.domains.chat.persistence.entity.DeviceToken;
import kr.co.isajjim.global.annotation.UseCase;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class DeviceTokenUseCase {

    private final DeviceTokenService deviceTokenService;

    public void registerDeviceToken(Long userId, DeviceTokenRequest request) {
        DeviceToken deviceToken = DeviceTokenMapper.toDeviceToken(userId, request);
        deviceTokenService.registerDeviceToken(deviceToken);
    }

    public void unregisterDeviceToken(DeviceTokenRequest request) {
        deviceTokenService.unregisterDeviceToken(request.token(), request.deviceType());
    }
}
