package kr.co.isajjim.domains.credit.persistence.repository;

import jakarta.persistence.LockModeType;
import kr.co.isajjim.domains.credit.persistence.entity.CreditBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CreditBalanceRepository extends JpaRepository<CreditBalance, Long> {

    // 잔액 조회 전용 (GET 응답 등). 락을 걸거나 행을 생성하지 않는다.
    Optional<CreditBalance> findByUser_Id(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM CreditBalance b WHERE b.user.id = :userId")
    Optional<CreditBalance> findByUser_IdForUpdate(@Param("userId") Long userId);
}
