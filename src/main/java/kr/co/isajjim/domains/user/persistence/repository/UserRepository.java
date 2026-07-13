package kr.co.isajjim.domains.user.persistence.repository;

import kr.co.isajjim.domains.user.domain.constant.Role;
import kr.co.isajjim.domains.user.persistence.entity.UserEntity;
import kr.co.isajjim.global.security.constant.SocialProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId);

    @Query("""
            SELECT u FROM UserEntity u
            WHERE (:role IS NULL OR u.role = :role)
            AND (:keyword IS NULL
                OR u.name LIKE CONCAT('%', :keyword, '%')
                OR u.email LIKE CONCAT('%', :keyword, '%'))
            """)
    Page<UserEntity> search(
            @Param("role") Role role,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}