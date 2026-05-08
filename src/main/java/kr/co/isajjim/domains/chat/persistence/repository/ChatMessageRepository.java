package kr.co.isajjim.domains.chat.persistence.repository;

import kr.co.isajjim.domains.chat.persistence.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Slice<ChatMessage> findByChatRoomIdOrderByCreatedDateDesc(Long chatRoomId, Pageable pageable);
}
