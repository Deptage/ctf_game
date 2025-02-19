package put.edu.ctfgame.homepage.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import put.edu.ctfgame.homepage.dto.HintDTO;
import put.edu.ctfgame.homepage.entity.hint.PlayerHint;
import put.edu.ctfgame.homepage.entity.hint.PlayerHintKey;
import put.edu.ctfgame.homepage.exception.HintAlreadyRevealedException;
import put.edu.ctfgame.homepage.exception.HintDoesNotExistException;
import put.edu.ctfgame.homepage.repository.HintRepository;
import put.edu.ctfgame.homepage.repository.PlayerHintRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class HintService {

    private final HintRepository hintRepository;
    private final PlayerHintRepository playerHintRepository;
    private final UserService userService;

    public List<HintDTO> findAll() {
        var currentUser = userService.findAuthenticated();
        var hints = hintRepository.findAll();
        return hints.stream()
                .map(hint -> {
                    var playerHint = playerHintRepository.findByUserAndHint(currentUser, hint);
                    return HintDTO.from(hint, playerHint.isPresent());
                })
                .toList();
    }

    public List<HintDTO> findAllByFlagId(Long flagId) {
        var currentUser = userService.findAuthenticated();
        var hints = hintRepository.findAllByFlagId(flagId);
        return hints.stream()
                .map(hint -> {
                    var playerHint = playerHintRepository.findByUserAndHint(currentUser, hint);
                    return HintDTO.from(hint, playerHint.isPresent());
                })
                .toList();
    }

    public HintDTO revealHint(Long flagId) {
        var currentUser = userService.findAuthenticated();
        var hint = hintRepository.findNextByFlagId(flagId, currentUser.getId()).orElseThrow(
                () -> new HintDoesNotExistException("Hint not found")
        );
        var playerHint = playerHintRepository.findByUserAndHint(currentUser, hint);
        if (playerHint.isPresent()) {
            throw new HintAlreadyRevealedException("Hint already revealed");
        }

        playerHintRepository.save(PlayerHint.builder()
                .id(PlayerHintKey.builder()
                        .userId(currentUser.getId())
                        .hintId(hint.getId())
                        .build())
                .hint(hint)
                .user(currentUser)
                .build());
        return HintDTO.from(hint, true);
    }
}