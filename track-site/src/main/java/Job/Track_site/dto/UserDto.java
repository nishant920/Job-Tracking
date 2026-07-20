package Job.Track_site.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
    // @NotBlank ensures name is not null, not empty (""), and contains non-whitespace characters
    @NotBlank(message = "Name is required")
    private String name;

    // @Email validates proper email format (e.g. user@domain.com)
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    // @Size enforces minimum and maximum string length constraints
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}
