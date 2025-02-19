package put.edu.ctfgame.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.edu.ctfgame.forum.entity.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDTO {
    private String name;

    public static RoleDTO from(Role role) {
        return RoleDTO.builder()
                .name(role.getName())
                .build();
    }
}