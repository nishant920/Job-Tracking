package Job.Track_site.dto;

import Job.Track_site.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JobStatusDto {
    @NotNull(message = "Job status is required")
    private Status status;
}
