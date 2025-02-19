package put.edu.ctfgame.homepage.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import put.edu.ctfgame.homepage.dto.FlagSolvedInfo;
import put.edu.ctfgame.homepage.entity.Flag;
import put.edu.ctfgame.homepage.exception.NoSuchFlagException;
import put.edu.ctfgame.homepage.repository.FlagRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class FlagService {

    private final FlagRepository flagRepository;
    private final UserService userService;

    public Flag findByIdOrElseThrow(Long id) {
        return flagRepository.findById(id).orElseThrow(
                () -> new NoSuchFlagException("Flag with id " + id + " does not exist")
        );
    }

    public List<FlagSolvedInfo> getFlagsSolvedInfo() {
        var currentUser = userService.findAuthenticated();
        return flagRepository.getFlagsSolvedInfo(currentUser.getId());
    }

}
