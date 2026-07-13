package kr.co.isajjim.domains.user.persistence.entity;

import jakarta.persistence.*;
import kr.co.isajjim.domains.user.domain.constant.ApprovalStatus;
import kr.co.isajjim.global.base.entity.BaseEntity;
import lombok.*;

@Entity
@Getter
@Table(
        name = "partner_profiles",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_business_registration_number", columnNames = {"business_registration_number"})
        }
)
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartnerProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partner_profile_id")
    private Long id;

    // 파트너(업체)로 전환한 유저 1명당 프로필 1개.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    // 업체명
    private String companyName;

    // 대표자명
    private String representativeName;

    // 사업자등록번호
    private String businessRegistrationNumber;

    // 사업장 주소
    private String businessAddress;

    // 업체 연락처 (고객 문의용, 유저 계정의 소셜 정보와 별개)
    private String contactPhone;

    // 업체 소개
    @Column(columnDefinition = "TEXT")
    private String introduction;

    // 사업자등록증 사진 (S3 URL)
    private String businessRegistrationImageUrl;

    // 관리자 승인 여부. 승인 전까지는 파트너 권한이 있어도 노출/매칭에서 제외.
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    public static PartnerProfile create(UserEntity user, String companyName, String representativeName,
                                         String businessRegistrationNumber, String businessAddress, String contactPhone,
                                         String introduction, String businessRegistrationImageUrl) {
        return PartnerProfile.builder()
                .user(user)
                .companyName(companyName)
                .representativeName(representativeName)
                .businessRegistrationNumber(businessRegistrationNumber)
                .businessAddress(businessAddress)
                .contactPhone(contactPhone)
                .introduction(introduction)
                .businessRegistrationImageUrl(businessRegistrationImageUrl)
                .approvalStatus(ApprovalStatus.PENDING)
                .build();
    }

    // 신청 정보 수정. 반려(REJECTED) 상태였던 신청을 수정하는 경우에만 재검토 대상이 되도록 PENDING으로 되돌린다.
    // (APPROVED 상태는 role이 이미 PARTNER로 전환되어 이 메서드 자체가 호출되지 않음)
    public void updateProfile(String companyName, String representativeName, String businessRegistrationNumber,
                               String businessAddress, String contactPhone, String introduction,
                               String businessRegistrationImageUrl) {
        this.companyName = companyName;
        this.representativeName = representativeName;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.businessAddress = businessAddress;
        this.contactPhone = contactPhone;
        this.introduction = introduction;
        this.businessRegistrationImageUrl = businessRegistrationImageUrl;

        if (this.approvalStatus == ApprovalStatus.REJECTED) {
            this.approvalStatus = ApprovalStatus.PENDING;
        }
    }

    public void approve() {
        this.approvalStatus = ApprovalStatus.APPROVED;
    }

    public void reject() {
        this.approvalStatus = ApprovalStatus.REJECTED;
    }
}
