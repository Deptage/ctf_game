package put.edu.ctfgame.company.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import put.edu.ctfgame.company.dto.WorkerDTO;
import put.edu.ctfgame.company.service.WorkerService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/workers")
public class WorkerController {
    private final WorkerService workerService;

    @GetMapping
    @CrossOrigin(origins="*")
    public ResponseEntity<List<WorkerDTO>> findAll() {
        return ResponseEntity.ok(workerService.findAll());
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins="*")
    public ResponseEntity<WorkerDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(workerService.findById(id));
    }

}
