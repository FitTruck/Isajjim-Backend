package kr.co.isajjim.domains.chat.persistence.entity;

import jakarta.persistence.*;
import kr.co.isajjim.global.base.entity.BaseEntity;
import lombok.AccessLevel;
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

    @Column(nullable = false)
    private String token;

    public static DeviceToken create(Long userId, String token) {
        DeviceToken deviceToken = new DeviceToken();
        deviceToken.userId = userId;
        deviceToken.token = token;
        return deviceToken;
    }

    public void update(String token) {
        this.token = token;
    }
}
