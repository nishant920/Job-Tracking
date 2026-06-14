package Job.Track_site.dto;

import Job.Track_site.enums.Status;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class JobDto {
    private String profile;
    private String company;
    private Double salary;
    private Status status;
    private int experience;

    private List<String> skillSet;

    private String platform;
    private LocalDate appliedDate;

}
