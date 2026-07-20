package Job.Track_site.dto;

import Job.Track_site.enums.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class JobDto {
    // @NotBlank ensures String field is not null, not empty (""), and contains non-whitespace text
    @NotBlank(message = "Job profile is required")
    private String profile;

    // @NotNull ensures object reference is not null
    // @Valid triggers cascading validation on nested CompanyDto object fields (e.g. company.name)
    @NotNull(message = "Company details are required")
    @Valid
    private CompanyDto company;

    // @NotNull ensures Double field is not null
    // @PositiveOrZero validates numeric values >= 0
    @NotNull(message = "Salary is required")
    @PositiveOrZero(message = "Salary must be zero or positive")
    private Double salary;

    // @NotNull ensures Enum status is provided
    @NotNull(message = "Job status is required")
    private Status status;

    // @PositiveOrZero ensures int value is >= 0
    @PositiveOrZero(message = "Experience cannot be negative")
    private int experience;

    private List<String> skillSet;

    private String platform;
    private LocalDate appliedDate;
}
