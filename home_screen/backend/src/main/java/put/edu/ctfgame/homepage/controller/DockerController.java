package put.edu.ctfgame.homepage.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.web.bind.annotation.*;


import lombok.AllArgsConstructor;
import net.minidev.json.JSONObject;
import put.edu.ctfgame.homepage.dto.ServerInstanceDTO;
import put.edu.ctfgame.homepage.entity.ServerInstance;
import put.edu.ctfgame.homepage.enums.LevelName;
import put.edu.ctfgame.homepage.exception.InvalidInstanceNameException;
import put.edu.ctfgame.homepage.exception.LevelAlreadyRunningException;
import put.edu.ctfgame.homepage.exception.LevelNotRunningException;
import put.edu.ctfgame.homepage.service.InstanceService;
import put.edu.ctfgame.homepage.service.JwtService;
import put.edu.ctfgame.homepage.service.PuppeteerService;
import java.time.LocalDateTime;
import put.edu.ctfgame.homepage.service.DockerService;

@RequestMapping("/instances")
@RestController
@AllArgsConstructor
@Slf4j
public class DockerController {

    private final InstanceService instanceService;
    private final DockerService dockerService;
    private final PuppeteerService puppeteerService;
    private final JwtService jwtService;

    private final Object multipleStartGuard = new Object();
    private final Object portSelectingGuard = new Object();

    private final int uptime = 6000;

    @GetMapping("/status")
    public ResponseEntity<ServerInstanceDTO> status(HttpServletRequest request) {
        String username = jwtService.extractUsernameFromRequest(request);
        var instance = instanceService.getInstanceByUsername(username);

        return ResponseEntity.ok(ServerInstanceDTO.from(instance));
    }

    @GetMapping("/runLevel")
    public ResponseEntity<Object> runLevel(@RequestParam String levelName, HttpServletRequest request) {
        var validLevelName = validateLevelName(levelName);
        var username = jwtService.extractUsernameFromRequest(request);

        int frontPort = switch (validLevelName) {
            case MESSENGER -> 1111;
            case BANK -> 2222;
            case FORUM -> 3333;
            case COMPANY -> 4444;
        };

        ServerInstance instance;
        synchronized(multipleStartGuard){
            if (instanceService.existsByUsername(username)) {
                throw new LevelAlreadyRunningException("Level already running");
            }
            instance = getReservedServerInstance(username, frontPort);
        }

        int backPort = instance.getBackendPort();
        String instanceId = instance.getId();

        JSONObject userInfoJSON = new JSONObject();

        userInfoJSON.put("instance_id", instanceId);   

        String userInfo = Base64.getEncoder().encodeToString(userInfoJSON.toJSONString().getBytes());

        switch (validLevelName) {
            case MESSENGER ->{ 
                dockerService.runDockerCompose("/aqua_messenger/", uptime, frontPort, backPort, instanceId);
                puppeteerService.startReadingMessages();
            }
            case BANK -> dockerService.runDockerCompose("/bank_page/", uptime, frontPort, backPort, instanceId);
            case FORUM -> dockerService.runDockerCompose("/forum/", uptime, frontPort, backPort, instanceId);
            case COMPANY -> dockerService.runDockerCompose("/company_page/", uptime, frontPort, backPort, instanceId);
        }
        log.info("id: {}", instanceId);
        log.info("Level started: {} by user: {}", validLevelName, username);
        log.info("Selected port: f:{} b:{}", frontPort, backPort);

        instance.setIsRunning(true);
        instanceService.save(instance);
        String url = "";
        switch (validLevelName){
            case MESSENGER -> url = "http://messenger.ctfgame.pl/start/"+userInfo;
            case BANK -> url = "http://bank.ctfgame.pl/start/"+userInfo;
            case FORUM -> url = "http://forum.ctfgame.pl/start/"+userInfo;
            case COMPANY -> url = "http://company.ctfgame.pl/start/"+userInfo;
        }

        JSONObject entity = new JSONObject();
        entity.put("URL", url); 
        return ResponseEntity.ok().body(entity);
    }

    @GetMapping("/stopLevel")
    public ResponseEntity<Object> stopLevel() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!instanceService.existsByUsername(username)){
            throw new LevelNotRunningException("Level is not running");
        }

        ServerInstance instance = instanceService.getInstanceByUsername(username);
        DockerService dockerUtil = new DockerService();
        log.info("id: {}", instance.getId().toString());
        instance.setUsername(null);
        instance.setFrontendPort(0);
        instance.setIPAddress(null);
        instanceService.save(instance);
        JSONObject entity = new JSONObject();
        entity.put("message", "Level stopped");

        //if instances are empty, stop reading messages
        if(instanceService.getAllInstanceFrontendPorts().length == 0){
            puppeteerService.stopReadingMessages();
        }
        dockerUtil.stopDockerCompose(instance.getId());
    
        return ResponseEntity.ok().body(entity);
    }

    @GetMapping("/extendTimeout")
    public ResponseEntity<Object> extendTimeout(HttpServletRequest request) {
        int additionalTime = uptime;
        String username = jwtService.extractUsernameFromRequest(request);
        ServerInstance instance = instanceService.getInstanceByUsername(username);
        log.info("Extending timeout for user: {}", username);
        if (instance == null) {
            return ResponseEntity.badRequest().body("Instance not found for user: " + username);
        }

        String instanceId = instance.getId();
        dockerService.extendTimeout(instanceId, additionalTime);
        instance.setCreatedAt(LocalDateTime.now());
        instanceService.save(instance);
        log.info("Instance creation time updated and saved for instance: {}", instanceId);
        return ResponseEntity.ok(ServerInstanceDTO.from(instance));
    }

    private LevelName validateLevelName(String levelName){
        try {
            return LevelName.valueOf(levelName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidInstanceNameException("Invalid level name");
        }
    }

    private ServerInstance getReservedServerInstance(String username, int frontPort) {
        //get any server instance from repository where username is null

        ServerInstance instance = instanceService.getFreeInstance();
        instance.setUsername(username);
        instance.setFrontendPort(frontPort);
        instance.setIsRunning(false);
        instance.setCreatedAt(LocalDateTime.now());
        instanceService.save(instance);

        return instance;
    }


}