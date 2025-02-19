package put.edu.ctfgame.company.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import put.edu.ctfgame.company.dto.WorkerDTO;
import put.edu.ctfgame.company.exception.WorkerNotFoundException;
import put.edu.ctfgame.company.repository.WorkerRepository;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class WorkerService {
    private final WorkerRepository workerRepository;

    public List<WorkerDTO> findAll() {
        log.info("Fetching all workers");
        return workerRepository.findAll().stream()
                .map(WorkerDTO::from)
                .collect(Collectors.toList());
    }

    public WorkerDTO findById(Long id) {
        log.info("Fetching worker by id: {}", id);
        return WorkerDTO.from(workerRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Worker with id {} not found", id);
                    return new WorkerNotFoundException("Worker with id " + id + " not found");
                }
        ));
    }
}
