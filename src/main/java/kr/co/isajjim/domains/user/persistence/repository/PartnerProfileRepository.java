package kr.co.isajjim.domains.user.persistence.repository;

import kr.co.isajjim.domains.user.domain.constant.ApprovalStatus;
import kr.co.isajjim.domains.user.persistence.entity.PartnerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PartnerProfileRepository extends JpaRepository<PartnerProfile, Long> {

    Optional<PartnerProfile> findByUser_Id(Long userId);

    boolean existsByUser_Id(Long userId);

    @Query("""
            SELECT p FROM PartnerProfile p
            WHERE (:approvalStatus IS NULL OR p.approvalStatus = :approvalStatus)
            AND (:keyword IS NULL
                OR p.companyName LIKE CONCAT('%', :keyword, '%')
                OR p.representativeName LIKE CONCAT('%', :keyword, '%'))
            """)
    Page<PartnerProfile> search(
            @Param("approvalStatus") ApprovalStatus approvalStatus,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
