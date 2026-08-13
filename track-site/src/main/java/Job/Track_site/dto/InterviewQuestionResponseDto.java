package Job.Track_site.dto;

import lombok.Data;

import java.util.List;

@Data
public class InterviewQuestionResponseDto {

    private String profile;
    private List<String> questions;
}
