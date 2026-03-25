package kr.co.isajjim.domains.refreshtoken.persistence.repository;

import kr.co.isajjim.domains.refreshtoken.persistence.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    // 기본 existsById를 사용해도 되지만, 서비스 로직에서의 가독성을 위해 명시적으로 선언
    boolean existsByRefreshToken(String refreshToken);
    void deleteByRefreshToken(String refreshToken);
    List<RefreshToken> findAllByUserId(Long userId);
}