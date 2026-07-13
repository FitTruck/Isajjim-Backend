package kr.co.isajjim.domains.credit.persistence.entity;

import jakarta.persistence.*;
import kr.co.isajjim.domains.credit.domain.constant.CreditChargeStatus;
import kr.co.isajjim.domains.user.persistence.entity.UserEntity;
import kr.co.isajjim.global.base.entity.BaseEntity;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(
        name = "credit_charge_orders",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_credit_charge_order_order_id", columnNames = {"order_id"})
        },
        indexes = {
                @Index(name = "idx_credit_charge_order_user", columnList = "user_id")
        }
)
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreditChargeOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credit_charge_order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // 서버가 생성하는 주문 식별자. 클라이언트가 지정한 값을 신뢰하지 않는다.
    @Column(name = "order_id", nullable = false, length = 64)
    private String orderId;

    private String orderName;

    // 원화 충전 요청 금액. 최초 요청 이후 불변 (confirm 시 변조 여부 대조 기준).
    private Long amount;

    // ready 시점의 환산 비율로 계산해 저장. 이후 비율 설정이 바뀌어도 이미 생성된 주문 가치는 변하지 않는다.
    private Long creditAmount;

    @Enumerated(EnumType.STRING)
    private CreditChargeStatus status;

    private String paymentKey;

    // Toss 응답의 status 원문 (감사/디버깅용)
    private String tossStatus;

    private LocalDateTime approvedAt;

    @Column(columnDefinition = "TEXT")
    private String failReason;

    public static CreditChargeOrder create(UserEntity user, String orderId, String orderName, Long amount, Long creditAmount) {
        return CreditChargeOrder.builder()
                .user(user)
                .orderId(orderId)
                .orderName(orderName)
                .amount(amount)
                .creditAmount(creditAmount)
                .status(CreditChargeStatus.READY)
                .build();
    }

    public void complete(String paymentKey, String tossStatus, LocalDateTime approvedAt) {
        assertReady();
        this.status = CreditChargeStatus.DONE;
        this.paymentKey = paymentKey;
        this.tossStatus = tossStatus;
        this.approvedAt = approvedAt;
    }

    public void fail(String failReason) {
        assertReady();
        this.status = CreditChargeStatus.FAILED;
        this.failReason = failReason;
    }

    private void assertReady() {
        if (this.status != CreditChargeStatus.READY) {
            throw new IllegalStateException("이미 처리된 크레딧 충전 주문입니다.");
        }
    }
}
