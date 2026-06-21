package Job.Track_site.repository;

import Job.Track_site.models.Job;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByProfile(String profile); //don't need optional here as list never  null if not found instead it is empty list
}
