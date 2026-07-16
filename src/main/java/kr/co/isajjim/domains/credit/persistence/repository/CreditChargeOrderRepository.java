package kr.co.isajjim.domains.credit.persistence.repository;

import jakarta.persistence.LockModeType;
import kr.co.isajjim.domains.credit.persistence.entity.CreditChargeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CreditChargeOrderRepository extends JpaRepository<CreditChargeOrder, Long> {

    Optional<CreditChargeOrder> findByOrderIdAndUser_Id(String orderId, Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM CreditChargeOrder o WHERE o.orderId = :orderId AND o.user.id = :userId")
    Optional<CreditChargeOrder> findByOrderIdAndUser_IdForUpdate(@Param("orderId") String orderId, @Param("userId") Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM CreditChargeOrder o WHERE o.id = :id")
    Optional<CreditChargeOrder> findByIdForUpdate(@Param("id") Long id);
}
