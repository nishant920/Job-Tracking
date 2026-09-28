package Job.Track_site.service;

import Job.Track_site.dto.JobDto;
import Job.Track_site.dto.JobResponseDto;
import Job.Track_site.dto.JobStatsDto;
import Job.Track_site.dto.JobStatusDto;
import Job.Track_site.enums.Status;
import Job.Track_site.exceptions.BadRequestException;
import Job.Track_site.exceptions.ResourceNotFoundException;
import Job.Track_site.exceptions.UnauthorizedException;
import Job.Track_site.models.Company;
import Job.Track_site.models.Job;
import Job.Track_site.models.User;
import Job.Track_site.repository.CompanyRepository;
import Job.Track_site.repository.JobRepository;
import Job.Track_site.utility.Mapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobService {
    JobRepository jobRepository;
    Mapper mapper;
    CompanyRepository companyRepository;

    public JobService(JobRepository jobRepository, Mapper mapper, CompanyRepository companyRepository){
        this.jobRepository =jobRepository;
        this.mapper=mapper;
        this.companyRepository=companyRepository;
    }
 public JobResponseDto createJob(JobDto jobDto){
        String companyName = jobDto.getCompany().getName();
        Company company = companyRepository.findByName(companyName);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if(company == null){
            company = mapper.mapCompanyDtoTOCompany(jobDto.getCompany());
            companyRepository.save(company);
        }
        Job job = mapper.mapJobDtoTOJob(jobDto, company);
        job.setUser(user);
        jobRepository.save(job);
        /*Now we need to set company in Job
        * 1> if the company alredy exixts in the database we map it to current job
        * 2>  */
        JobResponseDto jobResponseDto = mapper.mapJobToJobResponseDto(job);
        return jobResponseDto;
    }



 public Job updateStatus(Long id, JobStatusDto jobStatusDto){

     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

     if(authentication == null){
         throw new UnauthorizedException("User is not authenticated");
     }
     User user = (User) authentication.getPrincipal();

    Job job = jobRepository.findByIdAndUserId(id, user.getId()).orElseThrow(() -> new ResourceNotFoundException("Job not found")); // not using findById(id).orElse(null) becouse it will give null vales if no Job not

     if (jobStatusDto.getStatus() == null) {
         throw new BadRequestException("Job status is required");
     }
     job.setStatus(jobStatusDto.getStatus());//and throw nullpointerexeption here becouse null is allowed Blindly calling methods on null is not in java(look in codex)
     return jobRepository.save(job);
 }

 public List<JobResponseDto> getAllJobByProfile(String profile){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null){
            throw new UnauthorizedException("User is not authenticated");
        }
        User user = (User) authentication.getPrincipal();

        List<Job> jobs = jobRepository.findByUserIdAndProfile(user.getId(), profile);
        if(jobs.isEmpty()) {
            throw new ResourceNotFoundException("Job Profile is not found");
        }

        List<JobResponseDto> jobResponseDtos = new ArrayList<>();

        for(Job job : jobs) {
            JobResponseDto dto = mapper.mapJobToJobResponseDto(job);
            jobResponseDtos.add(dto);
        }
        return jobResponseDtos;

 }

 public void deleteJobById(Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null){
            throw new UnauthorizedException("User is not authenticated");
        }

        User user = (User) authentication.getPrincipal();

        Job job = jobRepository.findByIdAndUserId(id, user.getId()).orElseThrow(() -> new ResourceNotFoundException("No job mapped to user"));

        jobRepository.delete(job);
 }

 public JobStatsDto getJobStats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new UnauthorizedException("User is not authenticated");
        }

        User user = (User) authentication.getPrincipal();

        List<Object[]> results = jobRepository.countJobsByStatusForUser(user.getId());

        long applied = 0;
        long interviewed = 0;
        long rejected = 0;
        long offer = 0;
        long noResponse = 0;
        long total = 0;

        for (Object[] row : results) {
            Status status = (Status) row[0];
            long count = ((Number) row[1]).longValue();
            total += count;

            switch (status) {
                case APPLIED -> applied = count;
                case INTERVIEWED -> interviewed = count;
                case REJECTED -> rejected = count;
                case OFFER -> offer = count;
                case NO_RESPONSE -> noResponse = count;
            }
        }

        return JobStatsDto.builder()
                .total(total)
                .applied(applied)
                .interviewed(interviewed)
                .rejected(rejected)
                .offer(offer)
                .noResponse(noResponse)
                .build();
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