package Job.Track_site.dto;

import Job.Track_site.models.Job;
import lombok.Data;


import java.util.List;

@Data
public class CompanyDto {

    private String name;
    private String location;
    private List<Job> jobs;
}
