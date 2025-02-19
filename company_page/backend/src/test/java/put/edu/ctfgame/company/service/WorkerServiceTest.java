package put.edu.ctfgame.company.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import put.edu.ctfgame.company.TestDataFactory;
import put.edu.ctfgame.company.dto.WorkerDTO;
import put.edu.ctfgame.company.entity.Worker;
import put.edu.ctfgame.company.exception.WorkerNotFoundException;
import put.edu.ctfgame.company.repository.WorkerRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkerServiceTest {

    @Mock
    private WorkerRepository workerRepository;

    @InjectMocks
    private WorkerService workerService;

    private Worker worker;
    private WorkerDTO workerDTO;

    @BeforeEach
    void setUp() {
        worker = TestDataFactory.sampleWorker();
        workerDTO = WorkerDTO.from(worker);
    }

    @Test
    void testFindAll() {
        // When
        when(workerRepository.findAll()).thenReturn(List.of(worker));

        // Then
        var result = workerService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(workerDTO, result.get(0));
    }

    @Test
    void testFindById() {
        // When
        when(workerRepository.findById(1L)).thenReturn(Optional.of(worker));

        // Then
        var result = workerService.findById(1L);
        assertNotNull(result);
        assertEquals(workerDTO, result);
    }

    @Test
    void testFindByIdNotFound() {
        // When
        when(workerRepository.findById(1L)).thenReturn(Optional.empty());

        // Then
        assertThrows(WorkerNotFoundException.class, () -> workerService.findById(1L));
    }
}