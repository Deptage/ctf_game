package put.edu.ctfgame.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import put.edu.ctfgame.bank.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
