package kr.co.isajjim.domains.chat.persistence.repository;

import kr.co.isajjim.domains.chat.persistence.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT r FROM ChatRoom r WHERE (r.creatorId = :id1 AND r.targetId = :id2) OR (r.creatorId = :id2 AND r.targetId = :id1)")
    Optional<ChatRoom> findByParticipants(@Param("id1") Long id1, @Param("id2") Long id2);

    List<ChatRoom> findByCreatorIdOrTargetIdOrderByLastMessageAtDesc(Long creatorId, Long targetId);
}
