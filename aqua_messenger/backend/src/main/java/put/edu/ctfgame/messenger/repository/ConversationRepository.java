package put.edu.ctfgame.messenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import put.edu.ctfgame.messenger.entity.Conversation;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("SELECT c FROM Conversation c WHERE (c.user1.username = :user1Name AND c.user2.username = :user2Name) OR (c.user1.username = :user2Name AND c.user2.username = :user1Name)")
    Optional<Conversation> findConversationByUsernames(@Param("user1Name") String user1Name, @Param("user2Name") String user2Name);

    @Query("SELECT c FROM Conversation c WHERE c.user1.username = :username OR c.user2.username = :username")
    List<Conversation> findConversationsByUsername(@Param("username") String username);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM Conversation c WHERE (c.user1.username = :user1Name AND c.user2.username = :user2Name) OR (c.user1.username = :user2Name AND c.user2.username = :user1Name)")
    Boolean existsByUsernames(String user1Name, String user2Name);

}
