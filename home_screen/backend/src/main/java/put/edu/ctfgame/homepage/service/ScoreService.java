package put.edu.ctfgame.homepage.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import put.edu.ctfgame.homepage.dto.FlagInput;
import put.edu.ctfgame.homepage.dto.FlagResponse;
import put.edu.ctfgame.homepage.entity.Flag;
import put.edu.ctfgame.homepage.entity.score.Score;
import put.edu.ctfgame.homepage.entity.score.ScoreKey;
import put.edu.ctfgame.homepage.repository.FlagRepository;
import put.edu.ctfgame.homepage.repository.PlayerHintRepository;
import put.edu.ctfgame.homepage.repository.ScoreRepository;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final UserService userService;
    private final PlayerHintRepository playerHintRepository;
    private final FlagService flagService;
    private final MailService mailService;

    @Transactional
    public FlagResponse submitFlag(Long flagId, FlagInput input) {
        Flag flag = flagService.findByIdOrElseThrow(flagId);
        if (!flag.getFlag().equals(input.getFlag())) {
            return FlagResponse.builder()
                    .correct(false)
                    .message("Incorrect!")
                    .build();
        }
        var currentUser = userService.findAuthenticated();
        if (scoreRepository.existsByFlagAndUser(flag, currentUser)) {
            return FlagResponse.builder()
                    .correct(false)
                    .message("You have already completed this level!")
                    .build();
        }

        //var penalty = playerHintRepository.sumPointsLossByUsernameAndFlagId(currentUser.getUsername(), flag.getId());
        var points = playerHintRepository.calculateRemainingPoints(currentUser.getUsername(), flag.getId());
        submitToExternalServer(currentUser.getUsername(), flag.getFlag(), currentUser.getUniversity(), String.valueOf(points));
        scoreRepository.save(Score.builder()
                .id(ScoreKey.builder()
                        .userId(currentUser.getId())
                        .flagId(flag.getId())
                        .build())
                .points(points)
                .flag(flag)
                .user(currentUser)
                .build());
        mailService.handleRevealAfterFlagSubmitted(flag);
        return FlagResponse.builder()
                .correct(true)
                .message("Correct! Slay!")
                .build();
    }

    public void submitToExternalServer(String username, String flag, String university, String points) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("https://put-ctf-competition.put.poznan.pl/index.php?rest_route=/submit/v1/data");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");

            Map<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("flag", flag);
            params.put("university", university);
            params.put("points", points);
            StringBuilder result = new StringBuilder();

            for (Map.Entry<String, String> entry : params.entrySet()) {
                result.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
                result.append("&");
            }
            result.deleteCharAt(result.length() - 1);

            log.info(result.toString());
            connection.setRequestProperty("Content-Length", Integer.toString(result.length()));
            connection.setDoOutput(true);
            try (DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
                out.writeBytes(result.toString());
                out.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = connection.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                    log.info(response.toString());
                }
            } else {
                log.error("Failed to submit to external server. HTTP response code: " + responseCode);
            }
        } catch (FileNotFoundException e) {
            log.error("File not found: " + e.getMessage());
        } catch (IOException e) {
            log.error("IO Exception: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error submitting to external server: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}