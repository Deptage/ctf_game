package put.edu.ctfgame.bank.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import put.edu.ctfgame.bank.dto.AccountDTO;
import put.edu.ctfgame.bank.entity.Account;
import put.edu.ctfgame.bank.entity.BankUser;
import put.edu.ctfgame.bank.repository.AccountRepository;
import put.edu.ctfgame.bank.util.AccountNumberGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;

    public List<AccountDTO> findAll() {
        log.info("Fetching all accounts at {}", LocalDateTime.now());
        var accounts = accountRepository.findAll();
        log.info("Fetched {} accounts", accounts.size());
        return accounts.stream().map(AccountDTO::from).toList();
    }

    public List<AccountDTO> findAuthenticated() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (BankUser) authentication.getPrincipal();
        log.info("Fetching accounts for user {} at {}", currentUser.getUsername(), LocalDateTime.now());
        var accounts = accountRepository.findByUser(currentUser);
        log.info("Fetched {} accounts", accounts.size());
        return accounts.stream().map(AccountDTO::from).toList();
    }

    public AccountDTO createAccount(Float initBalance) {
        if (initBalance == null || initBalance < 0) {
            initBalance = 100.0f;
        }
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (BankUser) authentication.getPrincipal();
        log.info("Creating account for user {} at {}", currentUser.getUsername(), LocalDateTime.now());

        var account = Account.builder()
                .user(currentUser)
                .balance(initBalance)
                .accountNumber(generateUniqueAccountNumber())
                .build();
        var savedAccount = accountRepository.save(account);
        log.info("Created account with number {} for user {}", savedAccount.getAccountNumber(), currentUser.getUsername());
        return AccountDTO.from(savedAccount);
    }

    private String generateUniqueAccountNumber() {
        String accountNumber;
        log.debug("Generating unique account number");
        do {
            accountNumber = AccountNumberGenerator.generateAccountNumberWithCheckDigit();
            log.debug("Generated account number {}", accountNumber);
        } while (accountRepository.existsByAccountNumber(accountNumber));
        log.debug("Generated unique account number {}", accountNumber);
        return accountNumber;
    }

    public Float getMaxAccountBalance(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (BankUser) authentication.getPrincipal();
        var accounts = accountRepository.findByUser(currentUser);
        Float maxBalance = 0.0f;
        for (Account account : accounts) {
            if (account.getBalance() > maxBalance) {
                maxBalance = account.getBalance();
            }
        }
        return maxBalance;
    }

    public List<String> getAccountNumbersByUser(BankUser user) {
        List<Account> accounts = accountRepository.findByUser(user);
        log.info("Found {} accounts", accounts.size());
        List<String> accountNumbers = new ArrayList<>();
        for (Account account : accounts) {
            accountNumbers.add(account.getAccountNumber());
        }
        return accountNumbers;
    }
}
