package kr.co.isajjim.domains.chat.persistence.entity;

import jakarta.persistence.*;
import kr.co.isajjim.domains.chat.domain.constant.DeviceType;
import kr.co.isajjim.global.base.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "device_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeviceToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_token_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceType deviceType;

    @Builder
    private DeviceToken(Long userId, String token, DeviceType deviceType) {
        this.userId = userId;
        this.token = token;
        this.deviceType = deviceType;
    }
}
