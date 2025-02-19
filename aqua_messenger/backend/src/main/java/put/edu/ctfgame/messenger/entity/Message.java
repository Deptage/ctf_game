package put.edu.ctfgame.messenger.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Message")
public class Message {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User from;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column
    private LocalDateTime sentAt;

    @Column(nullable = false)
    @Builder.Default
    private Boolean sent = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "message_sequence",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "next_message_id")
    )
    @Builder.Default
    private Set<Message> nextMessages = new HashSet<>();

    @ManyToMany(mappedBy = "nextMessages", fetch = FetchType.EAGER)
    @Builder.Default
    private Set<Message> previousMessages = new HashSet<>();

    public void addNextMessage(Message message) {
        nextMessages.add(message);
        message.getPreviousMessages().add(this);
    }

    public void removeNextMessage(Message message) {
        nextMessages.remove(message);
        message.getPreviousMessages().remove(this);
    }


    // Override toString to prevent infinite recursion
    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", conversation=" + (conversation != null ? conversation.getId() : null) +
                ", sender=" + (from != null ? from.getUsername() : null) +
                ", content='" + content + '\'' +
                ", sentAt=" + sentAt +
                ", sent=" + sent +
                '}';
    }

    // Override equals and hashCode to prevent infinite recursion
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message message)) return false;
        return getId() != null && Objects.equals(getId(), message.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
