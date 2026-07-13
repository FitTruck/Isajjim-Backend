package kr.co.isajjim.domains.user.persistence.repository;

import kr.co.isajjim.domains.user.domain.constant.ApprovalStatus;
import kr.co.isajjim.domains.user.persistence.entity.PartnerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartnerProfileRepository extends JpaRepository<PartnerProfile, Long> {

    Optional<PartnerProfile> findByUser_Id(Long userId);

    boolean existsByUser_Id(Long userId);

    Page<PartnerProfile> findAllByApprovalStatus(ApprovalStatus approvalStatus, Pageable pageable);
}
