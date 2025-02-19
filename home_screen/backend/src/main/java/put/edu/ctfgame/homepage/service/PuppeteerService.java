package put.edu.ctfgame.homepage.service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.integration.IntegrationProperties.RSocket.Server;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import put.edu.ctfgame.homepage.entity.ServerInstance;
import put.edu.ctfgame.homepage.util.SystemCommand;

import java.io.IOException;
import net.minidev.json.JSONObject;

import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class PuppeteerService {
    private final InstanceService instanceService;
    private ScheduledExecutorService scheduler = null;
    
    public void startReadingMessages() {
        if (scheduler == null || scheduler.isShutdown()) {
            scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    readMessages();
                } catch (IOException | io.jsonwebtoken.io.IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }, 2, 1, TimeUnit.MINUTES);
            log.info("Started reading messages every 5 minutes.");
        } else {
            log.info("Reading messages is already scheduled.");
        }
    }

    public void stopReadingMessages() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            log.info("Stopped reading messages.");
        } else {
            log.info("Reading messages is not currently scheduled.");
        }
    }
    
    private void readMessages() throws IOException, io.jsonwebtoken.io.IOException, InterruptedException {
        //get instance list 
        ServerInstance[] instances = instanceService.getAllInstances();

        for (ServerInstance serverInstance : instances) {
            if(serverInstance.getFrontendPort() == 1111){
                String id = serverInstance.getId();
                String ip = serverInstance.getIPAddress();

                if(ip == null){
                    ip = getIPAddressFromInspect(id+"-backend-1");
                    ip = ip.replace("\"", "");
                    log.info("IP: {}", ip);
                    serverInstance.setIPAddress(ip);
                    instanceService.save(serverInstance);
                }
                JSONObject userInfoJSON = new JSONObject();
                userInfoJSON.put("instance_id", id);
                String userInfo = Base64.getEncoder().encodeToString(userInfoJSON.toJSONString().getBytes());
                String osName = System.getProperty("os.name");
                String url;
                
                //url = "http://host.docker.internal:1111/start/"+userInfo;//for windows host
            
                url = "http://172.17.0.1:1111/start/"+userInfo; //for linux host



            
            String b64Url = Base64.getEncoder().encodeToString(userInfoJSON.toJSONString().getBytes());
            log.info("Received request to read message");
            String status = "success";
            String backendURL = id+".ctfgame.pl";
            
            String command = String.format(
                "sudo -u root /usr/bin/docker run --add-host=%s:%s --rm -e B64URL=%s --cap-add=SYS_ADMIN --network=%s headless",
                backendURL, ip, url, id+"_messenger-network"
            );
            log.info("Command: {}", command);
            SystemCommand.run(List.of(command.split(" ")), true, false);
            log.info("Finished reading message  with status {}", status);
            }
        }
    }

    private String getIPAddressFromInspect(String id) throws IOException, InterruptedException {
        String command = "sudo -u root /usr/bin/docker inspect -f {{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}} " + id;
        log.info("Command: {}", command);
        List<String> out = SystemCommand.runAndGetOutput(List.of(command.split(" ")), true);
        log.info("debug: {}", out); 
        return out.get(0);
    }
}
