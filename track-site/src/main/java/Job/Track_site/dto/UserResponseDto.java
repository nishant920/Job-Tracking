package Job.Track_site.dto;

import Job.Track_site.enums.Role;
import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private boolean verified;
    private Role role;
}
