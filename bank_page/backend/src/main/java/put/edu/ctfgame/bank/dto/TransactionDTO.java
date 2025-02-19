package put.edu.ctfgame.bank.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.edu.ctfgame.bank.entity.Transaction;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {
    private Long id;

    @NotNull(message = "Amount is required")
    @Digits(integer = 10, fraction = 2, message = "Invalid amount")
    @Min(value = 1, message = "Amount must be greater than 0")
    private Float amount;

    private String title;

    private LocalDateTime createdAt;

    @NotNull(message = "Source account is required")
    private String sourceAccountNumber;

    @NotNull(message = "Destination account is required")
    private String destinationAccountNumber;

    public static TransactionDTO from(Transaction transaction) {
        return TransactionDTO.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .title(transaction.getTitle())
                .createdAt(transaction.getCreatedAt())
                .sourceAccountNumber(transaction.getSourceAccount().getAccountNumber())
                .destinationAccountNumber(transaction.getDestinationAccount().getAccountNumber())
                .build();
    }
}
