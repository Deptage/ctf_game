package put.edu.ctfgame.messenger.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import put.edu.ctfgame.messenger.entity.User;

@Data
@AllArgsConstructor
@Builder
public class UsernameDTO {
    private String username;

    public static UsernameDTO from(User user) {
        return UsernameDTO.builder()
                .username(user.getUsername())
                .build();
    }
}
