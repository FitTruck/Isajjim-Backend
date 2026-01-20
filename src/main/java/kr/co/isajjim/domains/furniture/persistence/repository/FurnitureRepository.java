package kr.co.isajjim.domains.furniture.persistence.repository;

import kr.co.isajjim.domains.furniture.persistence.entity.Furniture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FurnitureRepository extends JpaRepository<Furniture, Long> {
}
