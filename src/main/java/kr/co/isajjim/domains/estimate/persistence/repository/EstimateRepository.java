package kr.co.isajjim.domains.estimate.persistence.repository;

import kr.co.isajjim.domains.estimate.domain.constant.AIStatus;
import kr.co.isajjim.domains.estimate.persistence.entity.Estimate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {

    List<Estimate> findAllByUserId(Long userId);

    Page<Estimate> findAllByAiStatus(AIStatus aiStatus, Pageable pageable);
}
