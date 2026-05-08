package kr.co.isajjim.domains.chat.persistence.repository;

import kr.co.isajjim.domains.chat.persistence.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByUserIdAndVendorId(Long userId, Long vendorId);
    List<ChatRoom> findByUserIdOrVendorIdOrderByLastMessageAtDesc(Long userId, Long vendorId);
}
