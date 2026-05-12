package kr.co.isajjim.domains.chat.persistence.repository;

import kr.co.isajjim.domains.chat.domain.constant.DeviceType;
import kr.co.isajjim.domains.chat.persistence.entity.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {

    Optional<DeviceToken> findByTokenAndDeviceType(String token, DeviceType deviceType);

    List<DeviceToken> findAllByUserId(Long userId);

    void deleteByTokenAndDeviceType(String token, DeviceType deviceType);

    @Modifying
    @Query("DELETE FROM DeviceToken dt WHERE dt.token IN :tokens")
    void deleteAllByTokenIn(List<String> tokens);
}
