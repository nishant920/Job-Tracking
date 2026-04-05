package Job.Track_site.models;

import Job.Track_site.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name="job")
public class Job {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false)
    private String profile;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    @Column(nullable = false)
    private Double salary;

    @Enumerated(EnumType.STRING)
    private Status status; //Applied, Interview, Rejected
    private int experience;

    @ElementCollection
    private List<String> skillSet;

    private String platform; //Linkedin, Naukri, Indeed
    private LocalDate appliedDate;

}
