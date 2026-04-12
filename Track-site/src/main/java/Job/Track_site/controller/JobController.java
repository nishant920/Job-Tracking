package Job.Track_site.controller;

import Job.Track_site.dto.JobDto;
import Job.Track_site.dto.JobStatusDto;
import Job.Track_site.enums.Status;
import Job.Track_site.models.Job;
import Job.Track_site.service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/job")
public class JobController {
    JobService jobService;
    public JobController(JobService jobService){
        this.jobService=jobService;
    }
    @PostMapping("/save")
    public ResponseEntity<Job> createJob(@RequestBody JobDto jobDto){
      Job job =jobService.createJob(jobDto);
      return new ResponseEntity<Job>(job, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Job> updateJobStatus(@PathVariable Long id,  @RequestBody JobStatusDto jobStatusDto){
        Job updatedJob = jobService.updateStatus(id, jobStatusDto);
        return new ResponseEntity<>(updatedJob, HttpStatus.OK);
    }
}
