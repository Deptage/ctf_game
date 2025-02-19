package put.edu.ctfgame.homepage.util;

import java.util.ArrayList;
import java.util.List;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Map;

import io.jsonwebtoken.io.IOException;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SystemCommand {

    public static void run(List<String> command, boolean printOut, boolean waitForCompletion) throws IOException, InterruptedException {
        try {
            runWithEnv(command, printOut, waitForCompletion, null);
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void runWithEnv(List<String> command, boolean printOut, boolean waitForCompletion, Map<String, String> env) throws IOException, InterruptedException, java.io.IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);

        if (env != null) {
            processBuilder.environment().putAll(env);
        }

        Process process = processBuilder.start();

        if (printOut) {
            try (BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

                String line;
                while ((line = outputReader.readLine()) != null) {
                    log.info(line);
                }
                while ((line = errorReader.readLine()) != null) {
                    log.error(line);
                }
            }
        }

        if (waitForCompletion) {
            process.waitFor();
        }
    }

    public static List<String> runAndGetOutput(List<String> command, boolean waitForCompletion) throws IOException, InterruptedException, java.io.IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        
        List<String> outputLines = new ArrayList<>();
        BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        
        String line;
        while ((line = outputReader.readLine()) != null) {
            outputLines.add(line);
        }
        while ((line = errorReader.readLine()) != null) {
            outputLines.add(line);
        }

        if (waitForCompletion) {
            process.waitFor();
        }

        return outputLines;
    }

}