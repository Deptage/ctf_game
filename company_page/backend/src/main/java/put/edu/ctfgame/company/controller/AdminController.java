package put.edu.ctfgame.company.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/adminPanel")
@Slf4j
public class AdminController {

    @PostMapping
    @CrossOrigin(origins="*")
    public ResponseEntity<Object> auth(@RequestBody Map<String, String> params) {
        String param1 = params.get("login");
        String param2 = params.get("password");
    

        String[] command = {"sh",  "-c", "curl --data login=%s --data passwd=%s http://127.0.0.1:1337/oauth".formatted(param1, param2)};
        StringBuilder result = new StringBuilder();
        StringBuilder error = new StringBuilder();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            result.append("Failed to authenticate.\n");
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            BufferedReader errreader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errline;
            result.append("\nError (Please remove this on prod version): \n");
            while ((errline = errreader.readLine()) != null) {
                //error.append(errline);
                result.append(errline);
            }
            //log.info(error.toString());
            log.info(result.toString());

            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error executing curl command");
        }

        return ResponseEntity.ok(result.toString());
    }


}
