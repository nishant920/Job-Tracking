package Job.Track_site.configurations;

import Job.Track_site.enums.Role;
import Job.Track_site.enums.Status;
import Job.Track_site.models.Company;
import Job.Track_site.models.Job;
import Job.Track_site.models.User;
import Job.Track_site.repository.CompanyRepository;
import Job.Track_site.repository.JobRepository;
import Job.Track_site.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DemoDataSeeder {

    @Bean
    public CommandLineRunner sendDemoData(
            UserRepository userRepository,
            CompanyRepository companyRepository,
            JobRepository jobRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            String demoEmail = "demo@jobtrack.com";

            // If demo user already exists, do nothing
            if (userRepository.findByEmail(demoEmail) != null) {
                return;
            }

            // Create verified demo user
            User demoUser = new User();
            demoUser.setName("Demo User");
            demoUser.setEmail(demoEmail);
            demoUser.setVerified(true);
            demoUser.setPassword(passwordEncoder.encode("demo1234"));
            demoUser.setRole(Role.AppUser);
            userRepository.save(demoUser);

            Company google = companyRepository.findByName("Google");
            if (google == null) {
                google = new Company();
                google.setName("Google");
                google.setLocation("Bangalore");
                companyRepository.save(google);
            }

            Company amazon = companyRepository.findByName("Amazon");
            if (amazon == null) {
                amazon = new Company();
                amazon.setName("Amazon");
                amazon.setLocation("Hyderabad");
                companyRepository.save(amazon);
            }

            Job job1 = new Job();
            job1.setUser(demoUser);
            job1.setCompany(google);
            job1.setProfile("Backend Developer");
            job1.setStatus(Status.INTERVIEWED);
            job1.setSalary(1500000.0);
            job1.setExperience(3);
            job1.setSkillSet(List.of("Java", "Spring Boot", "PostgreSQL", "Docker"));
            job1.setPlatform("LinkedIn");
            job1.setAppliedDate(LocalDate.now().minusDays(5));
            jobRepository.save(job1);

            Job job2 = new Job();
            job2.setUser(demoUser);
            job2.setCompany(amazon);
            job2.setProfile("Software Engineer");
            job2.setStatus(Status.APPLIED);
            job2.setSalary(1800000.0);
            job2.setExperience(2);
            job2.setSkillSet(List.of("Java", "Microservices", "AWS"));
            job2.setPlatform("Company Portal");
            job2.setAppliedDate(LocalDate.now().minusDays(2));
            jobRepository.save(job2);
        };
    }
}
