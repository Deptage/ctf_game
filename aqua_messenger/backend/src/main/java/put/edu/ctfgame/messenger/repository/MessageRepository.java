package put.edu.ctfgame.messenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import put.edu.ctfgame.messenger.entity.Message;
import put.edu.ctfgame.messenger.entity.Conversation;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversation(Conversation conversation);

    @Query("SELECT m.nextMessages FROM Message m " +
            "WHERE m.conversation = ?1 AND m.sent = true " +
            "AND m.sentAt = (SELECT MAX(m2.sentAt) FROM Message m2 " +
            "               WHERE m2.conversation = ?1 AND m2.sent = true)")
    List<Message> findNextMessagesOfLastSentInConversation(Conversation conversation);

    List<Message> findAllByConversationIdAndSentTrueOrderBySentAtAsc(Long conversationId);
}
