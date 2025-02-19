package put.edu.ctfgame.messenger.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import put.edu.ctfgame.messenger.DTO.MessageDTO;
import put.edu.ctfgame.messenger.enums.ConversationType;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "conversation")
public class Conversation {

    @Id 
    @GeneratedValue
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConversationType type;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "conversation")
    @Builder.Default
    private Set<Message> messages = new HashSet<>();

    public MessageDTO addMessage(Message message) {
        messages.add(message);
        return MessageDTO.from(message);
    }

    public Conversation(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }
}
