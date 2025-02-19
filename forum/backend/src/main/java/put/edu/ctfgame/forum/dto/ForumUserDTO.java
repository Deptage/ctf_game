package put.edu.ctfgame.forum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.edu.ctfgame.forum.entity.ForumUser;
import put.edu.ctfgame.forum.enums.RoleEnum;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ForumUserDTO {

    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Builder.Default
    private Set<RoleDTO> roles = new HashSet<>();

    public static ForumUserDTO from(ForumUser user) {
        return ForumUserDTO.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRoles().stream().map(RoleDTO::from).collect(Collectors.toSet()))
            .build();
    }
}
