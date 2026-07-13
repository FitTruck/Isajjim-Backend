package kr.co.isajjim.domains.credit.persistence.entity;

import jakarta.persistence.*;
import kr.co.isajjim.domains.user.persistence.entity.UserEntity;
import kr.co.isajjim.global.base.entity.BaseEntity;
import lombok.*;

@Entity
@Getter
@Table(name = "credit_balances")
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreditBalance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credit_balance_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    private Long balance;

    public static CreditBalance createEmpty(UserEntity user) {
        return CreditBalance.builder()
                .user(user)
                .balance(0L)
                .build();
    }

    public void increase(Long amount) {
        this.balance += amount;
    }

    public void decrease(Long amount) {
        if (this.balance < amount) {
            throw new IllegalStateException("보유 크레딧이 부족합니다.");
        }
        this.balance -= amount;
    }
}
