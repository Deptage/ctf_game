package put.edu.ctfgame.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.edu.ctfgame.bank.entity.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDTO {
    String name;

    public static RoleDTO from(Role role) {
        return RoleDTO.builder()
                .name(role.getName())
                .build();
    }
}
