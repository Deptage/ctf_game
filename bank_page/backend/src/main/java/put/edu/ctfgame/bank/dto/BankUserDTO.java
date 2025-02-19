package put.edu.ctfgame.bank.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.edu.ctfgame.bank.entity.BankUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BankUserDTO {

    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "Password is required")
    private String password;

    @Builder.Default
    private List<AccountDTO> accounts = new ArrayList<>();

    @Builder.Default
    private Set<RoleDTO> roles = new HashSet<>();

    public static BankUserDTO from(BankUser user) {
        return BankUserDTO.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .accounts(user.getAccounts().stream().map(AccountDTO::from).toList())
                .roles(user.getRoles().stream().map(RoleDTO::from).collect(Collectors.toSet()))
                .build();
    }
}
