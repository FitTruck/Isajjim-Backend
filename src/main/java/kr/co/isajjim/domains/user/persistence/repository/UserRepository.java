package kr.co.isajjim.domains.user.persistence.repository;

import kr.co.isajjim.domains.user.domain.constant.Role;
import kr.co.isajjim.domains.user.persistence.entity.UserEntity;
import kr.co.isajjim.global.security.constant.SocialProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId);

    Page<UserEntity> findAllByRole(Role role, Pageable pageable);
}