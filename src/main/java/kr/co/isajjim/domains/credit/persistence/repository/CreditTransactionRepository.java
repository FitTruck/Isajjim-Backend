package kr.co.isajjim.domains.credit.persistence.repository;

import kr.co.isajjim.domains.credit.persistence.entity.CreditTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditTransactionRepository extends JpaRepository<CreditTransaction, Long> {

    Page<CreditTransaction> findByUser_Id(Long userId, Pageable pageable);

    Page<CreditTransaction> findByUser_IdIn(List<Long> userIds, Pageable pageable);
}
