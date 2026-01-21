package kr.co.isajjim.domains.image.persistence.repository;

import kr.co.isajjim.domains.image.persistence.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByEstimateId(Long estimateId);
}
