package Job.Track_site.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class InterviewQuestionDto {

    @NotBlank(message = "Job profile is required")
    private String profile;

    @PositiveOrZero(message = "Experience cannot be negative")
    private Integer experience; // e.g. 3 (returns mid-level questions)

    private String companyName; // e.g. "Google" (returns Google-specific questions)
}

/*@NotBlank on profile: This is perfect. Since the profile is required to build the prompt, this prevents clients from sending null, "", or "   ".
@PositiveOrZero on Integer experience: This is also correct. Since you used the object wrapper type Integer (instead of primitive int),
 it allows experience to be optional (null is allowed if the user doesn't specify experience), but forces it to be 0 or positive if it is provided.*/