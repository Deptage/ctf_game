package put.edu.ctfgame.homepage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import put.edu.ctfgame.homepage.entity.ServerInstance;
import put.edu.ctfgame.homepage.exception.LevelNotRunningException;
import put.edu.ctfgame.homepage.repository.ServerRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InstanceServiceTest {

    @Mock
    private ServerRepository serverRepository;

    @InjectMocks
    private InstanceService instanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetInstanceById() {
        ServerInstance instance = new ServerInstance();
        when(serverRepository.findById(anyString())).thenReturn(Optional.of(instance));

        ServerInstance result = instanceService.getInstanceById("testId");
        assertNotNull(result);
        assertEquals(instance, result);

        when(serverRepository.findById(anyString())).thenReturn(Optional.empty());
        result = instanceService.getInstanceById("testId");
        assertNull(result);
    }

    @Test
    void testGetInstanceByUsername() {
        ServerInstance instance = new ServerInstance();
        when(serverRepository.findByUsername(anyString())).thenReturn(Optional.of(instance));

        ServerInstance result = instanceService.getInstanceByUsername("testUser");
        assertNotNull(result);
        assertEquals(instance, result);

        when(serverRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        assertThrows(LevelNotRunningException.class, () -> instanceService.getInstanceByUsername("testUser"));
    }

    @Test
    void testExistsByUsername() {
        when(serverRepository.existsByUsername(anyString())).thenReturn(true);
        assertTrue(instanceService.existsByUsername("testUser"));

        when(serverRepository.existsByUsername(anyString())).thenReturn(false);
        assertFalse(instanceService.existsByUsername("testUser"));
    }

    @Test
    void testExistsByPort() {
        when(serverRepository.existsByBackendPortOrFrontendPort(anyInt(), anyInt())).thenReturn(true);
        assertTrue(instanceService.existsByPort(8080));

        when(serverRepository.existsByBackendPortOrFrontendPort(anyInt(), anyInt())).thenReturn(false);
        assertFalse(instanceService.existsByPort(8080));
    }

    @Test
    void testSave() {
        ServerInstance instance = new ServerInstance();
        when(serverRepository.save(any(ServerInstance.class))).thenReturn(instance);

        ServerInstance result = instanceService.save(instance);
        assertNotNull(result);
        assertEquals(instance, result);
    }

    @Test
    void testDelete() {
        ServerInstance instance = new ServerInstance();
        doNothing().when(serverRepository).delete(any(ServerInstance.class));

        instanceService.delete(instance);
        verify(serverRepository, times(1)).delete(instance);
    }
}