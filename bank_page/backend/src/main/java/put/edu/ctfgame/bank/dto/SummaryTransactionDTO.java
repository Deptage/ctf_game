package put.edu.ctfgame.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SummaryTransactionDTO {
    private String createdAt;
    private String amount;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private String title;
}
