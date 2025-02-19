package put.edu.ctfgame.bank;

import put.edu.ctfgame.bank.dto.AccountDTO;
import put.edu.ctfgame.bank.dto.BankUserDTO;
import put.edu.ctfgame.bank.dto.SummaryTransactionDTO;
import put.edu.ctfgame.bank.dto.TransactionDTO;
import put.edu.ctfgame.bank.entity.Account;
import put.edu.ctfgame.bank.entity.BankUser;
import put.edu.ctfgame.bank.entity.Role;
import put.edu.ctfgame.bank.entity.Transaction;

import java.time.LocalDateTime;
import java.util.Set;

public class TestDataFactory {

    public static Role sampleRole() {
        return Role.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();
    }

    public static BankUser sampleBankUser() {
        return BankUser.builder()
                .id("UUID1")
                .roles(Set.of(sampleRole()))
                .username("testuser")
                .password("password")
                .build();
    }

    public static Account sampleAccount() {
        return Account.builder()
                .id(1L)
                .balance(1000.0f)
                .createdAt(LocalDateTime.MAX)
                .accountNumber("123")
                .user(sampleBankUser())
                .build();
    }

    public static Account sampleAccount2() {
        return Account.builder()
                .id(2L)
                .balance(500.0f)
                .createdAt(LocalDateTime.MAX)
                .accountNumber("456")
                .user(sampleBankUser())
                .build();
    }

    public static AccountDTO sampleAccountDTO() {
        return AccountDTO.from(sampleAccount());
    }

    public static BankUserDTO sampleBankUserDTO() {
        return BankUserDTO.from(sampleBankUser());
    }

    public static Transaction sampleTransaction() {
        return Transaction.builder()
                .id(1L)
                .amount(100.0f)
                .createdAt(LocalDateTime.MAX)
                .sourceAccount(sampleAccount())
                .destinationAccount(sampleAccount2())
                .build();
    }

    public static TransactionDTO sampleTransactionDTO() {
        return TransactionDTO.from(sampleTransaction());
    }

    public static SummaryTransactionDTO sampleSummaryTransactionDTO() {
        return SummaryTransactionDTO.builder()
                .amount("$100.0")
                .createdAt("9999-12-31 23:59:59")
                .title("Sample Title")
                .sourceAccountNumber("123")
                .destinationAccountNumber("456")
                .build();
    }
}