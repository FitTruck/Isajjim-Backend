package kr.co.isajjim.domains.credit.persistence.repository;

import kr.co.isajjim.domains.credit.persistence.entity.CreditTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditTransactionRepository extends JpaRepository<CreditTransaction, Long> {

    Page<CreditTransaction> findByUser_Id(Long userId, Pageable pageable);
}
