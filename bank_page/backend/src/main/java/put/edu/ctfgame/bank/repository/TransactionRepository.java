package put.edu.ctfgame.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import put.edu.ctfgame.bank.entity.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.title LIKE %?1% AND (t.sourceAccount.user.username = ?2 OR t.destinationAccount.user.username = ?2)")
    List<Transaction> findByUsernameAndTitleContaining(String text, String username);

    List<Transaction> findAllBySourceAccountUserUsernameOrDestinationAccountUserUsername(String sourceUsername, String destinationUsername);
}
