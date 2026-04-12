package Job.Track_site.service;

import Job.Track_site.dto.JobDto;
import Job.Track_site.dto.JobStatusDto;
import Job.Track_site.models.Job;
import Job.Track_site.repository.JobRepository;
import Job.Track_site.utility.Mapper;
import org.springframework.stereotype.Service;

@Service
public class JobService {
    JobRepository jobRepository;
    Mapper mapper;

    public JobService(JobRepository jobRepository, Mapper mapper){
        this.jobRepository =jobRepository;
        this.mapper=mapper;
    }
 public Job createJob(JobDto jobDto){

        Job job = mapper.mapJobDtoTOJob(jobDto);
        return jobRepository.save(job);
 }

 public Job updateStatus(Long id, JobStatusDto jobStatusDto){
    Job job = jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found")); // not using findById(id).orElse(null) becouse it will give null vales if no Job not

     job.setStatus(jobStatusDto.getStatus());//and throw nullpointerexeption here becouse null is allowed Blindly calling methods on null is not in java(look in codex)
     return jobRepository.save(job);
 }

}

/*

                                                           short-note on RuntimeException and lamdaExpression
>RuntimeException and its subclasses are known as Unchecked Exceptions.
NullPointerException - popular RuntimeException
A lambda expression consists of three parts:

Parameters: The input variables.

The Arrow Operator: -> (the "becomes" or "goes to" operator).

The Body: The expressions or statements to be executed

*/