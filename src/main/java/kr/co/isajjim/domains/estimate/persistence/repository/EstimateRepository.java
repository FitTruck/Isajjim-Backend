package kr.co.isajjim.domains.estimate.persistence.repository;

import kr.co.isajjim.domains.estimate.persistence.entity.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {
}
