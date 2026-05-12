package kr.co.isajjim.domains.chat.domain.service;

import kr.co.isajjim.domains.chat.domain.constant.DeviceType;
import kr.co.isajjim.domains.chat.persistence.entity.DeviceToken;
import kr.co.isajjim.domains.chat.persistence.repository.DeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceTokenService {

    private final DeviceTokenRepository deviceTokenRepository;

    @Transactional
    public void registerDeviceToken(DeviceToken deviceToken) {
        Optional<DeviceToken> existing = deviceTokenRepository.findByTokenAndDeviceType(
                deviceToken.getToken(), deviceToken.getDeviceType());
        if (existing.isPresent()) {
            if (!existing.get().getUserId().equals(deviceToken.getUserId())) {
                deviceTokenRepository.delete(existing.get());
                deviceTokenRepository.flush();
                deviceTokenRepository.save(deviceToken);
            }
            // 동일 유저의 동일 토큰이면 그대로 유지
            return;
        }
        deviceTokenRepository.save(deviceToken);
    }

    @Transactional
    public void unregisterDeviceToken(String token, DeviceType deviceType) {
        deviceTokenRepository.deleteByTokenAndDeviceType(token, deviceType);

    }

    @Transactional
    public void deleteInvalidTokens(List<String> tokens) {
        if (!tokens.isEmpty()) {
            deviceTokenRepository.deleteAllByTokenIn(tokens);
        }
    }
}
