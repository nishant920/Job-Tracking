package Job.Track_site.repository;

import Job.Track_site.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
       /*
        Spring Data JPA follows a "naming convention" strategy.
        When it sees a method starting with findBy,
        it automatically parses the rest of the method name to generate the SQL query
        (SELECT * FROM users WHERE email = ?)
        */
    User findByEmail(String email);
}
