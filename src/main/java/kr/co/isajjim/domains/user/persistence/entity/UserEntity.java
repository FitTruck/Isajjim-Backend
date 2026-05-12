package kr.co.isajjim.domains.user.persistence.entity;

import jakarta.persistence.*;
import kr.co.isajjim.domains.estimate.persistence.entity.Estimate;
import kr.co.isajjim.domains.user.domain.constant.Role;
import kr.co.isajjim.domains.user.domain.constant.UserStatus;
import kr.co.isajjim.global.base.entity.BaseEntity;
import kr.co.isajjim.global.security.constant.SocialProvider;
import kr.co.isajjim.global.utils.DatabaseEncryptionConverter;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_social", columnNames = {"social_provider", "social_id"})
        }
)
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {

    // DB 확장은 현재 단계에서 고려하지 않음
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name;

    private String email;

    // 소셜 제공자.
    // 이메일은 고유하지 않음. 카카오 로그인으로 hyojae@naver.com로 가입을 하고 네이버 계정으로 hyojae@naver.com으로 가입할 수 있기때문.
    // 따라서 필자는 SocialProvider:socialId를 고유 ID로 사용하여 조회한다. (리포지토리 구현 후술)
    @Enumerated(EnumType.STRING)
    private SocialProvider socialProvider;

    // 소셜 제공자에서 반환하는 고유 ID.
    private String socialId;

    @Convert(converter = DatabaseEncryptionConverter.class)
    private String socialRefreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Estimate> estimates = new ArrayList<>();

    public static UserEntity socialSignup(String name, String email, SocialProvider socialProvider, String socialId, UserStatus status) {
        return UserEntity.builder()
                .name(name)
                .email(email)
                .socialProvider(socialProvider)
                .socialId(socialId)
                .role(Role.USER)
                .status(status)
                .build();
    }

    public void completeSignup() {
        this.status = UserStatus.ACTIVE;
    }

    public void updateSocialRefreshToken(String socialRefreshToken) {
        this.socialRefreshToken = socialRefreshToken;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateProfileImage(String profileImage) {
        this.profileImageUrl = profileImage;
    }

    public void deleteProfileImage() {
        this.profileImageUrl = null;
    }
}