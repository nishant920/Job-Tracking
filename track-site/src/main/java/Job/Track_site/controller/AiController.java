package Job.Track_site.controller;

import Job.Track_site.dto.InterviewQuestionDto;
import Job.Track_site.exceptions.BadRequestException;
import Job.Track_site.models.User;
import Job.Track_site.service.AiService;
import Job.Track_site.service.RateLimitingService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
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
    RateLimitingService rateLimitingService;

    private AiController(AiService aiService, RateLimitingService rateLimitingService){
        this.aiService=aiService;
        this.rateLimitingService=rateLimitingService;
    }

    @PostMapping("/interview-questions")
    public ResponseEntity<List<String>> jobInterviewResponse(@Valid @RequestBody InterviewQuestionDto interviewDto, HttpServletRequest request){

            String clintIp = request.getRemoteAddr();

            Bucket bucket = rateLimitingService.resolveBucket(clintIp);

            //tryConsume(1) is a method provided by the Bucket4j library. It attempts to take 1 token out of the user's bucket.
            //
            //It returns a boolean
             boolean tokenConsumed = bucket.tryConsume(1);
            if (!tokenConsumed) {
                throw new BadRequestException("Rate limit exceeded! You can only generate AI questions 5 times per hour.");
            }


            List<String> response = aiService.getQuestions(interviewDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
