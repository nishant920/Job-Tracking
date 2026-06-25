package Job.Track_site.service;

import Job.Track_site.dto.JobDto;
import Job.Track_site.dto.JobResponceDto;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
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
 public JobResponceDto createJob(JobDto jobDto){
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
        JobResponceDto jobResponceDto = mapper.jobToJobResponceDto(job);
        return jobResponceDto;
    }



 public Job updateStatus(Long id, JobStatusDto jobStatusDto){
    Job job = jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found")); // not using findById(id).orElse(null) becouse it will give null vales if no Job not

     job.setStatus(jobStatusDto.getStatus());//and throw nullpointerexeption here becouse null is allowed Blindly calling methods on null is not in java(look in codex)
     return jobRepository.save(job);
 }

 public List<JobResponceDto> getAllJobByProfile(String profile){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null){
            throw new RuntimeException("User is not authenticated");
        }
        User user = (User) authentication.getPrincipal();

        List<Job> jobs = jobRepository.findByUserIdAndProfile(user.getId(), profile);

        List<JobResponceDto> jobResponceDtos = new ArrayList<>();

        for(Job job : jobs) {
            JobResponceDto dto = mapper.jobToJobResponceDto(job);
            jobResponceDtos.add(dto);
        }
        return jobResponceDtos;

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