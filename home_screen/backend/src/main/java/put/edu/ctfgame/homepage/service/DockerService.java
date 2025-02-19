package put.edu.ctfgame.homepage.service;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.List;

import io.jsonwebtoken.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import put.edu.ctfgame.homepage.util.SystemCommand;

import java.util.Map;

@Slf4j
@Service
public class DockerService {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();


    public void runDockerCompose(String path, int timeout, int frontPort, int backPort, String instanceURI) {
        log.info("Starting Docker Compose with path: {}, timeout: {}, frontPort: {}, backPort: {}, instanceURI: {}", path, timeout, frontPort, backPort, instanceURI);
        try {
            log.info("Running Docker Compose up command");

            // Set environment variables
            Map<String, String> env = new HashMap<>();

            env.put("bPORT", String.valueOf(backPort));
            env.put("INSTANCE_ID", instanceURI);
            if(path.contains("messenger")){
            SystemCommand.runWithEnv(List.of("sudo",
                    "-u", "root",
                    "/usr/bin/docker",
                    "compose",
                    "-p", instanceURI,
                    "-f", path + "docker-compose-all.yml",
                    "up", "-d", "db", "backend", "csrf"), true, true, env);
          }
          else{
                        SystemCommand.runWithEnv(List.of("sudo",
                    "-u", "root",
                    "/usr/bin/docker",
                    "compose",
                    "-p", instanceURI,
                    "-f", path + "docker-compose-all.yml",
                    "up", "-d", "db", "backend"), true, true, env);
          }
          

            log.info("Docker Compose up command executed successfully for instanceURI: {}", instanceURI);

            ScheduledFuture<?> scheduledTask = scheduler.schedule(() -> {
                try {
                    log.info("Scheduled task to stop Docker Compose after {} seconds", timeout);
                    stopDockerCompose(instanceURI);
                } catch (Exception e) {
                    log.error("Exception occurred while stopping Docker Compose: {}", e.getMessage(), e);
                }
            }, timeout, TimeUnit.SECONDS);

            scheduledTasks.put(instanceURI, scheduledTask);

        } catch (Exception e) {
            log.error("Exception occurred in runDockerCompose: {}", e.getMessage(), e);
        }
    }

    public void stopDockerCompose(String instanceURI) {
        log.info("Stopping Docker Compose for instanceURI: {}", instanceURI);
        try {
            log.info("This is the last instance, stopping all containers");
            SystemCommand.run(List.of("sudo", "-u", "root", "/usr/bin/docker", "compose", "-p", instanceURI, "down", "--rmi", "all", "--volumes"), true, true);
            log.info("Docker Compose down command executed successfully for instanceURI: {}", instanceURI);
        } catch (Exception e) {
            log.error("Exception occurred in stopDockerCompose: {}", e.getMessage(), e);
        }
    }

    public void extendTimeout(String instanceURI, int additionalTime) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(instanceURI);
        if (scheduledTask != null && !scheduledTask.isDone()) {
            scheduledTask.cancel(false);
            log.info("Extending timeout by {} seconds for instanceURI: {}", additionalTime, instanceURI);
            ScheduledFuture<?> newScheduledTask = scheduler.schedule(() -> {
                try {
                    log.info("Scheduled task to stop Docker Compose after extended timeout");
                    stopDockerCompose(instanceURI);
                } catch (Exception e) {
                    log.error("Exception occurred while stopping Docker Compose: {}", e.getMessage(), e);
                }
            }, additionalTime, TimeUnit.SECONDS);
            scheduledTasks.put(instanceURI, newScheduledTask);
        } else {
            log.warn("No active scheduled task to extend for instanceURI: {}", instanceURI);
        }
    }
}