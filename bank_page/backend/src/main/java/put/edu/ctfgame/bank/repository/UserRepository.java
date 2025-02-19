package put.edu.ctfgame.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import put.edu.ctfgame.bank.entity.BankUser;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<BankUser, Long> {
    Optional<BankUser> findByUsername(String username);
}
