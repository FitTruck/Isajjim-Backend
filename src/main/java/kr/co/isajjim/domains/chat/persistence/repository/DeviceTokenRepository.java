package kr.co.isajjim.domains.chat.persistence.repository;

import kr.co.isajjim.domains.chat.persistence.entity.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    Optional<DeviceToken> findByUserId(Long userId);
}
