package kr.co.isajjim.domains.credit.persistence.entity;

import jakarta.persistence.*;
import kr.co.isajjim.domains.credit.domain.constant.CreditTransactionType;
import kr.co.isajjim.domains.user.persistence.entity.UserEntity;
import kr.co.isajjim.global.base.entity.BaseEntity;
import lombok.*;

// 충전/소모 내역을 남기는 append-only 원장. 생성 후 필드를 변경하는 메서드는 두지 않는다.
@Entity
@Getter
@Table(
        name = "credit_transactions",
        indexes = {
                @Index(name = "idx_credit_tx_user_created", columnList = "user_id, created_date")
        }
)
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreditTransaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credit_transaction_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private CreditTransactionType type;

    // 항상 양수. 부호는 type으로 판단한다.
    private Long creditAmount;

    // 이 거래 처리 직후의 잔액 스냅샷 (분쟁/정산 대조용)
    private Long balanceAfter;

    // 이 거래를 발생시킨 출처 태그. 예: "TOSS_CHARGE", 추후 "ESTIMATE_DISPATCH" 등 호출자가 자유롭게 지정.
    private String referenceType;

    private Long referenceId;

    public static CreditTransaction charge(UserEntity user, Long creditAmount, Long balanceAfter, String referenceType, Long referenceId) {
        return of(user, CreditTransactionType.CHARGE, creditAmount, balanceAfter, referenceType, referenceId);
    }

    public static CreditTransaction consume(UserEntity user, Long creditAmount, Long balanceAfter, String referenceType, Long referenceId) {
        return of(user, CreditTransactionType.CONSUME, creditAmount, balanceAfter, referenceType, referenceId);
    }

    public static CreditTransaction refund(UserEntity user, Long creditAmount, Long balanceAfter, String referenceType, Long referenceId) {
        return of(user, CreditTransactionType.REFUND, creditAmount, balanceAfter, referenceType, referenceId);
    }

    private static CreditTransaction of(UserEntity user, CreditTransactionType type, Long creditAmount, Long balanceAfter, String referenceType, Long referenceId) {
        return CreditTransaction.builder()
                .user(user)
                .type(type)
                .creditAmount(creditAmount)
                .balanceAfter(balanceAfter)
                .referenceType(referenceType)
                .referenceId(referenceId)
                .build();
    }
}
