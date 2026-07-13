package kr.co.isajjim.domains.estimate.persistence.repository;

import kr.co.isajjim.domains.estimate.domain.constant.AIStatus;
import kr.co.isajjim.domains.estimate.persistence.entity.Estimate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {

    List<Estimate> findAllByUserId(Long userId);

    @Query("""
            SELECT e FROM Estimate e
            JOIN e.user u
            WHERE (:aiStatus IS NULL OR e.aiStatus = :aiStatus)
            AND (:keyword IS NULL
                OR u.name LIKE CONCAT('%', :keyword, '%')
                OR u.email LIKE CONCAT('%', :keyword, '%'))
            """)
    Page<Estimate> search(
            @Param("aiStatus") AIStatus aiStatus,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
