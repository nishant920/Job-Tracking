package Job.Track_site.utility;

import Job.Track_site.dto.JobDto;
import Job.Track_site.dto.JobStatusDto;
import Job.Track_site.dto.UserDto;
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

    public Job mapJobDtoTOJob(JobDto jobDto){
        Job job = new Job();
        job.setProfile(jobDto.getProfile());
        job.setCompany(jobDto.getCompany());
        job.setSalary(jobDto.getSalary());
        job.setStatus(jobDto.getStatus());
        job.setExperience(jobDto.getExperience());
        job.setSkillSet(jobDto.getSkillSet());
        job.setPlatform(jobDto.getPlatform());
        job.setAppliedDate(jobDto.getAppliedDate());

        return job;
    }

}
