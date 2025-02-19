package put.edu.ctfgame.company.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import put.edu.ctfgame.company.TestDataFactory;
import put.edu.ctfgame.company.dto.WorkerDTO;
import put.edu.ctfgame.company.service.WorkerService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@WebMvcTest(WorkerController.class)
class WorkerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkerService workerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() throws Exception {
        // Given
        var workers = List.of(WorkerDTO.from(TestDataFactory.sampleWorker()), WorkerDTO.from(TestDataFactory.sampleWorker()));
        when(workerService.findAll()).thenReturn(workers);

        // When & Then
        mockMvc.perform(get("/workers").header("Instance-Id", "instance-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    void testFindById() throws Exception {
        // Given
        var worker = WorkerDTO.from(TestDataFactory.sampleWorker());
        when(workerService.findById(1L)).thenReturn(worker);

        // When & Then
        mockMvc.perform(get("/workers/1").header("Instance-Id", "instance-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(worker.getName())));
    }
}