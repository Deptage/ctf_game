package put.edu.ctfgame.homepage.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import put.edu.ctfgame.homepage.entity.Level;
import put.edu.ctfgame.homepage.repository.LevelRepository;

@Service
@AllArgsConstructor
public class LevelService {
    private final LevelRepository levelRepository;
}
