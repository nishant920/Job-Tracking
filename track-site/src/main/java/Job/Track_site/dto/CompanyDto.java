package Job.Track_site.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CompanyDto {
    @NotBlank(message = "Company name is required")
    private String name;

    private String location;
}
