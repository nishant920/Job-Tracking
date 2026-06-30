package Job.Track_site.utility;

import Job.Track_site.dto.*;
import Job.Track_site.models.Company;
import Job.Track_site.models.Job;
import Job.Track_site.models.User;
import org.springframework.stereotype.Component;

@Component
public class Mapper {
    public User mapUserDetailsToUser(UserDto userDto){
      User user = new User();
      user.setName(userDto.getName());
      user.setEmail(userDto.getEmail());
      user.setPassword(userDto.getPassword());
      return user;
    }

    public Job mapJobDtoTOJob(JobDto jobDto, Company company){
        Job job = new Job();
        job.setProfile(jobDto.getProfile());
        job.setSalary(jobDto.getSalary());
        job.setStatus(jobDto.getStatus());
        job.setExperience(jobDto.getExperience());
        job.setSkillSet(jobDto.getSkillSet());
        job.setPlatform(jobDto.getPlatform());
        job.setAppliedDate(jobDto.getAppliedDate());
        job.setCompany(company);

        return job;
    }

    public Company mapCompanyDtoTOCompany(CompanyDto companyDto){
        Company company = new Company();

        company.setName(companyDto.getName());
        company.setLocation(companyDto.getLocation());
        return company;
    }


    public JobResponseDto jobToJobResponceDto(Job job) {
        JobResponseDto jobResponseDto = new JobResponseDto();
        jobResponseDto.setCompanyName(job.getCompany().getName());
        jobResponseDto.setStatus(job.getStatus());
        jobResponseDto.setAppliedDate(job.getAppliedDate());
        jobResponseDto.setSalary(job.getSalary());
        return jobResponseDto;
    }
}
