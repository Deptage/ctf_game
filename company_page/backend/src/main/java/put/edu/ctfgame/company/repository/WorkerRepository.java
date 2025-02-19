package put.edu.ctfgame.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import put.edu.ctfgame.company.entity.Worker;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
}
