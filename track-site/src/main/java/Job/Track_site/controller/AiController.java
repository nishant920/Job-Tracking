package Job.Track_site.controller;

import Job.Track_site.dto.InterviewQuestionDto;
import Job.Track_site.models.User;
import Job.Track_site.service.AiService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/ai/generate")
public class AiController {

    AiService aiService;

    private AiController(AiService aiService){
        this.aiService=aiService;
    }

    @PostMapping("/interview-questions")
    public ResponseEntity<List<String>> jobInterviewResponse(@Valid @RequestBody InterviewQuestionDto interviewDto){
            List<String> response = aiService.getQuestions(interviewDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
