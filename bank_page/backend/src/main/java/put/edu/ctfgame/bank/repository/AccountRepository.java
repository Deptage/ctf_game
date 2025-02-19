package put.edu.ctfgame.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import put.edu.ctfgame.bank.entity.Account;
import put.edu.ctfgame.bank.entity.BankUser;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String accountNumber);
    List<Account> findByUser(BankUser user);
    Optional<Account> findByAccountNumber(String accountNumber);

    @Transactional
    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance + :amount WHERE a.accountNumber = :accountNumber")
    void updateAccountBalance(String accountNumber, double amount);

    @Query("SELECT a.balance FROM Account a WHERE a.accountNumber = :accountNumber")
    float getAccountBalance(String accountNumber);
}
