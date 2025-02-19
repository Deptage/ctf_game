package put.edu.ctfgame.homepage.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import put.edu.ctfgame.homepage.dto.FlagInput;
import put.edu.ctfgame.homepage.dto.FlagResponse;
import put.edu.ctfgame.homepage.dto.FlagSolvedInfo;
import put.edu.ctfgame.homepage.service.FlagService;
import put.edu.ctfgame.homepage.service.ScoreService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/flag")
public class FlagController {

    private final ScoreService scoreService;
    private final FlagService flagService;

    @GetMapping("/solvedInfo")
    public ResponseEntity<List<FlagSolvedInfo>> getSolvedInfo() {
        return ResponseEntity.ok(flagService.getFlagsSolvedInfo());
    }

    @PostMapping("/submit/{flagId}")
    public ResponseEntity<FlagResponse> submitFlag(@PathVariable Long flagId, @Valid @RequestBody FlagInput input) {
        FlagResponse response = scoreService.submitFlag(flagId, input);
        return ResponseEntity.ok(response);
    }
}
