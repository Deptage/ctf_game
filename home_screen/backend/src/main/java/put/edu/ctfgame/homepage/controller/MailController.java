package put.edu.ctfgame.homepage.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import put.edu.ctfgame.homepage.dto.MailDTO;
import put.edu.ctfgame.homepage.service.MailService;

import java.util.List;

@RestController
@RequestMapping("/mail")
@AllArgsConstructor
public class MailController {

    private final MailService mailService;

    @GetMapping
    public ResponseEntity<List<MailDTO>> findAllVisibleForCurrentUser() {
        return ResponseEntity.ok(mailService.findAllVisibleForCurrentUser());
    }

    @PutMapping("/{mailId}/read")
    public ResponseEntity<Void> readMail(@PathVariable Long mailId) {
        mailService.readMailById(mailId);
        return ResponseEntity.ok().build();
    }
}
