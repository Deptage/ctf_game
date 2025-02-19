package put.edu.ctfgame.homepage.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.edu.ctfgame.homepage.entity.CtfgameUser;

import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RegisterDTO {

    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 4, message = "Username must contain at least 4 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9.-]{4,}@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Username must be a valid email address with at least 4 characters before the '@' character, containing only letters, digits, '.', or '-'"
    )
    @Email(message = "Username must be a valid email address")
    private String username;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must have at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])(?!.*[\\s\\\\/]).*$",
            message = "Password must have at least one lowercase letter, one uppercase letter, one digit, and one special character, and cannot contain spaces or slashes"
    )
    private String password;

    private String university;

    public static RegisterDTO from(CtfgameUser  user) {
        return RegisterDTO.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .university(user.getUniversity())
                .build();
    }
}

