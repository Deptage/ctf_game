package put.edu.ctfgame.homepage.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import put.edu.ctfgame.homepage.dto.HintDTO;
import put.edu.ctfgame.homepage.service.HintService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/hints")
public class HintController {

    private final HintService hintService;

    @GetMapping
    public ResponseEntity<List<HintDTO>> findAll() {
        return ResponseEntity.ok(hintService.findAll());
    }

    @GetMapping("/flag/{flagId}")
    public ResponseEntity<List<HintDTO>> findAllByFlagId(@PathVariable Long flagId) {
        return ResponseEntity.ok(hintService.findAllByFlagId(flagId));
    }

    @PostMapping("/flag/{flagId}/reveal")
    public ResponseEntity<HintDTO> revealHint(@PathVariable Long flagId) {
        return ResponseEntity.ok(hintService.revealHint(flagId));
    }
}
