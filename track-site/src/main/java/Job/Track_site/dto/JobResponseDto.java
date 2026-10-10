package Job.Track_site.dto;

import Job.Track_site.enums.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class JobResponseDto {
    Long id;
    String profile;
    LocalDate appliedDate;
    String companyName;
    Status status;
    double salary;
}
