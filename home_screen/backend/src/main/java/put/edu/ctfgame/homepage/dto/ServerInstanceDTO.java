package put.edu.ctfgame.homepage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.edu.ctfgame.homepage.entity.ServerInstance;

import java.time.Duration;
import java.time.LocalDateTime;

import net.minidev.json.JSONObject;
import java.util.Base64;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServerInstanceDTO {
    private int backendPort;
    private int frontendPort;
    private String username;
    private LocalDateTime createdAt;
    private long secondsRemaining;
    private String URL;
    private Boolean isRunning;


    public static ServerInstanceDTO from(ServerInstance serverInstance) {

        
        JSONObject userInfoJSON = new JSONObject();
        userInfoJSON.put("instance_id", serverInstance.getId());
        String userInfo = Base64.getEncoder().encodeToString(userInfoJSON.toJSONString().getBytes());
        String url = "";
        switch(serverInstance.getFrontendPort()){
            case 1111 -> url = "http://messenger.ctfgame.pl/start/"+userInfo;
            case 2222 -> url = "http://bank.ctfgame.pl/start/"+userInfo;
            case 3333 -> url = "http://forum.ctfgame.pl/start/"+userInfo;
            case 4444 -> url = "http://company.ctfgame.pl/start/"+userInfo;
        }

        return ServerInstanceDTO.builder()
                .backendPort(serverInstance.getBackendPort())
                .frontendPort(serverInstance.getFrontendPort())
                .username(serverInstance.getUsername())
                .createdAt(serverInstance.getCreatedAt())
                .isRunning(serverInstance.getIsRunning())
                .secondsRemaining(calculateSecondsRemaining(serverInstance.getCreatedAt()))
                .URL(url)
                .build();
    }

    private static long calculateSecondsRemaining(LocalDateTime createdAt) {
        var endTime = createdAt.plusHours(1);
        return Duration.between(LocalDateTime.now(), endTime).getSeconds();
    }
}
