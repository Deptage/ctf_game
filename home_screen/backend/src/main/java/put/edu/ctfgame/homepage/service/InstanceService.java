package put.edu.ctfgame.homepage.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import put.edu.ctfgame.homepage.entity.ServerInstance;
import put.edu.ctfgame.homepage.exception.LevelNotRunningException;
import put.edu.ctfgame.homepage.repository.ServerRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class InstanceService {
    private final ServerRepository serverRepository;

    public ServerInstance getInstanceById(String id) {
        log.info("Getting instance by ID: {}", id);
        Optional<ServerInstance> optInstance = serverRepository.findById(id);
        ServerInstance instance = optInstance.orElse(null);
        log.info("Got instance: {}", instance);
        return instance;
    }

    public ServerInstance getInstanceByUsername(String username) {
        log.info("Getting instance by username: {}", username);
        ServerInstance instance = serverRepository.findByUsername(username).orElseThrow(
                () -> {
                    log.error("Level not running for username: {}", username);
                    return new LevelNotRunningException("Level is not running");
                }
        );
        log.info("Got instance: {}", instance);
        return instance;
    }

    public ServerInstance getInstanceByFrontendPort(int frontendPort) {
        log.info("Getting instance by frontend port: {}", frontendPort);
        ServerInstance instance = serverRepository.findByFrontendPort(frontendPort).orElseThrow(
                () -> {
                    log.error("Level not running for frontend port: {}", frontendPort);
                    return new LevelNotRunningException("Level is not running");
                }
        );
        log.info("Got instance: {}", instance);
        return instance;
    }

    public int[] getAllInstanceFrontendPorts() {
        log.info("Getting all instance frontend ports");
        int[] frontendPorts = serverRepository.getAllFrontendPorts();
        log.info("Got all instance frontend ports: {}", frontendPorts);
        return frontendPorts;
    }

    public ServerInstance getFreeInstance() {
        log.info("Getting free instance");
        ServerInstance instance = serverRepository.findFirstByUsernameIsNull();
        log.info("Got free instance: {}", instance);
        return instance;
    }

    public ServerInstance[] getAllInstances() {
        log.info("Getting all instances");
        ServerInstance[] instances = serverRepository.getAllInstances();
        return instances;
    }
    
    public boolean existsByUsername(String username) {
        log.info("Checking if instance exists by username: {}", username);
        boolean exists = serverRepository.existsByUsername(username);
        log.info("Instance exists: {}", exists);
        return exists;
    }

    public boolean existsByPort(int port) {
        log.info("Checking if instance exists by port: {}", port);
        boolean exists = serverRepository.existsByBackendPortOrFrontendPort(port, port);
        log.info("Instance exists: {}", exists);
        return exists;
    }

    public ServerInstance save(ServerInstance serverInstance) {
        log.info("Saving instance: {}", serverInstance);
        ServerInstance savedInstance = serverRepository.save(serverInstance);
        log.info("Saved instance: {}", savedInstance);
        return savedInstance;
    }

    public void delete(ServerInstance serverInstance) {
        log.info("Deleting instance: {}", serverInstance);
        serverRepository.delete(serverInstance);
        log.info("Deleted instance");
    }
}