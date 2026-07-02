package Job.Track_site.service;

import Job.Track_site.dto.JobDto;
import Job.Track_site.dto.JobResponseDto;
import Job.Track_site.dto.JobStatusDto;
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
        JobResponseDto jobResponseDto = mapper.jobToJobResponceDto(job);
        return jobResponseDto;
    }



 public Job updateStatus(Long id, JobStatusDto jobStatusDto){

     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

     if(authentication == null){
         throw new RuntimeException("User is not authenticated");
     }
     User user = (User) authentication.getPrincipal();

    Job job = jobRepository.findByIdAndUserId(id, user.getId()).orElseThrow(() -> new RuntimeException("Job not found")); // not using findById(id).orElse(null) becouse it will give null vales if no Job not

     if (jobStatusDto.getStatus() == null) {
         throw new RuntimeException("Job status is required");
     }
     job.setStatus(jobStatusDto.getStatus());//and throw nullpointerexeption here becouse null is allowed Blindly calling methods on null is not in java(look in codex)
     return jobRepository.save(job);
 }

 public List<JobResponseDto> getAllJobByProfile(String profile){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null){
            throw new RuntimeException("User is not authenticated");
        }
        User user = (User) authentication.getPrincipal();

        List<Job> jobs = jobRepository.findByUserIdAndProfile(user.getId(), profile);

        List<JobResponseDto> jobResponseDtos = new ArrayList<>();

        for(Job job : jobs) {
            JobResponseDto dto = mapper.jobToJobResponceDto(job);
            jobResponseDtos.add(dto);
        }
        return jobResponseDtos;

 }

 public void deleteJobById(Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null){
            throw new RuntimeException("User is not authenticated");
        }

        User user = (User) authentication.getPrincipal();

        Job job = jobRepository.findByIdAndUserId(id, user.getId()).orElseThrow(() -> new RuntimeException("No job mapped to user"));

        jobRepository.delete(job);
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