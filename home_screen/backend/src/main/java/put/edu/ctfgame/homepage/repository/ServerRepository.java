package put.edu.ctfgame.homepage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import put.edu.ctfgame.homepage.entity.ServerInstance;

import java.util.Optional;

@Repository
public interface ServerRepository extends JpaRepository<ServerInstance, String> {
    Optional<ServerInstance> findByBackendPort(int backendPort);
    Optional<ServerInstance> findByFrontendPort(int frontendPort);
    Optional<ServerInstance> findByUsername(String username);
    @Query("SELECT s.frontendPort FROM ServerInstance s")
    int[] getAllFrontendPorts();
    @Query("SELECT s FROM ServerInstance s")
    ServerInstance[] getAllInstances();
    Boolean existsByBackendPortOrFrontendPort(int backendPort, int frontendPort);
    Boolean existsByUsername(String username);

    ServerInstance findFirstByUsernameIsNull();
}
