package put.edu.ctfgame.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.edu.ctfgame.bank.entity.Account;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO {
    private Long id;
    private String accountNumber;
    private Float balance;
    private LocalDateTime createdAt;
    private String userId;

    @Builder.Default
    private List<TransactionDTO> sourceTransactions = new ArrayList<>();

    @Builder.Default
    private List<TransactionDTO> destinationTransactions = new ArrayList<>();

    public static AccountDTO from(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .createdAt(account.getCreatedAt())
                .sourceTransactions(account.getSourceTransactions().stream().map(TransactionDTO::from).toList())
                .destinationTransactions(account.getDestinationTransactions().stream().map(TransactionDTO::from).toList())
                .userId(account.getUser().getId())
                .build();
    }
}
