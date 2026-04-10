package Job.Track_site.service;

import Job.Track_site.dto.JobDto;
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

}
