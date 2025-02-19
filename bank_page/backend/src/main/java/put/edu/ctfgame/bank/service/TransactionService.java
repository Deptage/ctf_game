package put.edu.ctfgame.bank.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import put.edu.ctfgame.bank.dto.SummaryTransactionDTO;
import put.edu.ctfgame.bank.dto.TransactionDTO;
import put.edu.ctfgame.bank.entity.Transaction;
import put.edu.ctfgame.bank.exception.InsufficientFundsException;
import put.edu.ctfgame.bank.exception.InvalidAccountException;
import put.edu.ctfgame.bank.repository.AccountRepository;
import put.edu.ctfgame.bank.repository.SusyTransactionRepository;
import put.edu.ctfgame.bank.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionService {
    private final AccountService accountService;
    private final UserService userService;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final SusyTransactionRepository susyTransactionRepository;

    public List<String> findAllAuthenticated() {
        var username = userService.findAuthenticated().getUsername();
        log.info("Fetching all transactions for user {}", username);
        var transactions = transactionRepository.findAllBySourceAccountUserUsernameOrDestinationAccountUserUsername(username, username).stream()
                .map(TransactionDTO::from)
                .map(TransactionDTO::toString)
                .toList();
        log.info("Fetched {} transactions", transactions.size());
        return transactions;
    }

    public List<TransactionDTO> findAll() {
        log.info("Fetching all transactions at {}", LocalDateTime.now());
        var transactions = transactionRepository.findAll().stream().map(TransactionDTO::from).collect(Collectors.toList());
        log.info("Fetched {} transactions", transactions.size());
        return transactions;
    }

    public List<SummaryTransactionDTO> findAllAuthenticatedByTitleContaining(String text) {
        var username = userService.findAuthenticated().getUsername();
        log.info("Fetching all transactions containing '{}' in title at {}", text, LocalDateTime.now());
        var transactions = susyTransactionRepository.findByUsernameAndTitleContaining(text, username);
        log.info("Fetched {} transactions", transactions.size());
        return transactions;
    }


    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        log.info("Creating transaction from account {} to account {} with amount {}",
                transactionDTO.getSourceAccountNumber(), transactionDTO.getDestinationAccountNumber(), transactionDTO.getAmount());

        var sourceAccount = accountRepository.findByAccountNumber(transactionDTO.getSourceAccountNumber())
                .orElseThrow(() -> {
                    log.error("Invalid sender account number: {}", transactionDTO.getSourceAccountNumber());
                    return new InvalidAccountException("Invalid sender account number");
                });

        var destinationAccount = accountRepository.findByAccountNumber(transactionDTO.getDestinationAccountNumber())
                .orElseThrow(() -> {
                    log.error("Invalid receiver account number: {}", transactionDTO.getDestinationAccountNumber());
                    return new InvalidAccountException("Invalid receiver account number");
                });

        if (sourceAccount.getBalance() < transactionDTO.getAmount()) {
            log.error("Insufficient funds in account {}: available balance {}, required amount {}",
                    transactionDTO.getSourceAccountNumber(), sourceAccount.getBalance(), transactionDTO.getAmount());
            throw new InsufficientFundsException("Insufficient funds");
        }

        try{
           //TODO: to specify after tests with burpsuite
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        accountRepository.updateAccountBalance(sourceAccount.getAccountNumber(), (-1) * transactionDTO.getAmount());
        accountRepository.updateAccountBalance(destinationAccount.getAccountNumber(), transactionDTO.getAmount());
        
        log.info(accountRepository.getAccountBalance(sourceAccount.getAccountNumber()) + " " + accountRepository.getAccountBalance(destinationAccount.getAccountNumber()));
        sourceAccount.setBalance(accountRepository.getAccountBalance(sourceAccount.getAccountNumber()));
        destinationAccount.setBalance(accountRepository.getAccountBalance(destinationAccount.getAccountNumber()));
        log.info(sourceAccount.getBalance().toString() + " " + destinationAccount.getBalance().toString());

        var transaction = Transaction.builder()
                .amount(transactionDTO.getAmount())
                .sourceAccount(sourceAccount)
                .destinationAccount(destinationAccount)
                .build();

        Optional.ofNullable(transactionDTO.getTitle())
                .ifPresent(transaction::setTitle);

        var savedTransaction = transactionRepository.save(transaction);

        log.info("Transaction created successfully with ID {}", savedTransaction.getId());
        return TransactionDTO.from(savedTransaction);
    }
}
