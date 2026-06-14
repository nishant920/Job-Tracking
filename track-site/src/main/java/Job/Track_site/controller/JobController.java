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

   /* this saves the Job in the database
     at this point 18/04/26,  didn't added user in JobDto so when I try to hit this end point it creates null in database,
     so to fix this I need to add JWT token based login so that only user can save job in the database and user_id feild dosen't set as null */
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
