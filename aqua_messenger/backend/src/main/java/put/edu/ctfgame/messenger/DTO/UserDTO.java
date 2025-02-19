package put.edu.ctfgame.messenger.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.edu.ctfgame.messenger.entity.User;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserDTO {

    private String username;
    private String password;
    private Boolean isBot;

    public static UserDTO from(User user) {
        return UserDTO.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .isBot(user.getIsBot())
                .build();
    }
}
