package put.edu.ctfgame.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.edu.ctfgame.forum.entity.ForumUser;
import put.edu.ctfgame.forum.entity.Role;

import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostAuthorDTO {
    private String username;
    private String roles;
    private Long postsCount;
    private Long commentsCount;

    public static PostAuthorDTO from(ForumUser user) {
        var roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        return PostAuthorDTO.builder()
            .username(user.getUsername())
            .roles(String.join(", ", roles))
            .build();
    }
}
