package kr.co.isajjim.domains.image.persistence.repository;

import kr.co.isajjim.domains.image.persistence.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
