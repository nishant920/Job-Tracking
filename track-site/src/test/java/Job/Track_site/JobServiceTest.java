package Job.Track_site;

import Job.Track_site.models.User;
import Job.Track_site.repository.CompanyRepository;
import Job.Track_site.repository.JobRepository;
import Job.Track_site.service.JobService;
import Job.Track_site.utility.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JobServiceTest {

    @Test
    void shouldReturnStatusUpdatedJob(){

        JobRepository jobRepository = mock(JobRepository.class);
        Mapper mapper = mock(Mapper.class);
        CompanyRepository companyRepository = mock(CompanyRepository.class);

        JobService jobService = new JobService(jobRepository, mapper, companyRepository);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        User user = new User();
        user.setId(1L);

         // Tell SecurityContextHolder to use our mocked SecurityContext
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);




    }
}
