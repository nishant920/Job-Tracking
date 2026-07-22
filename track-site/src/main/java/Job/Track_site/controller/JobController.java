package Job.Track_site.controller;

import Job.Track_site.dto.JobDto;
import Job.Track_site.dto.JobResponseDto;
import Job.Track_site.dto.JobStatusDto;
import Job.Track_site.models.Job;
import Job.Track_site.service.JobService;
import Job.Track_site.utility.Mapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/job")
public class JobController {
    JobService jobService;
    Mapper mapper;

    public JobController(JobService jobService, Mapper mapper){
        this.jobService=jobService;
        this.mapper=mapper;
    }

   /* this saves the Job in the database
     at this point 18/04/26,  didn't added user in JobDto so when I try to hit this end point it creates null in database,
     so to fix this I need to add JWT token based login so that only user can save job in the database and user_id feild dosen't set as null */
    @PostMapping("/save")
    public ResponseEntity<?> createJob(@Valid @RequestBody JobDto jobDto){
      JobResponseDto jobResponseDto =jobService.createJob(jobDto);
      return new ResponseEntity<>(jobResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<JobResponseDto> updateJobStatus(@PathVariable Long id, @Valid @RequestBody JobStatusDto jobStatusDto){
        Job updatedJob = jobService.updateStatus(id, jobStatusDto);
        JobResponseDto responseDto = mapper.mapJobToJobResponseDto(updatedJob);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobResponseDto>> getJobByProfile(@RequestParam(name = "profile") String profile){
        List<JobResponseDto> jobResponseDtos = jobService.getAllJobByProfile(profile);
        return new ResponseEntity<>(jobResponseDtos, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteByID(@PathVariable Long id){
        jobService.deleteJobById(id);
        return new ResponseEntity<>("Job is Deleted succesfully", HttpStatus.OK);
    }
}
