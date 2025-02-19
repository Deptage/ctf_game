package put.edu.ctfgame.bank.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    @Builder.Default
    private Float balance = 1000.0f;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private BankUser user;

    @OneToMany(mappedBy = "sourceAccount")
    @JsonManagedReference
    @Builder.Default
    private List<Transaction> sourceTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "destinationAccount")
    @JsonManagedReference
    @Builder.Default
    private List<Transaction> destinationTransactions = new ArrayList<>();
}
