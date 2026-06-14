package Job.Track_site.models;

import Job.Track_site.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

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
    @Column(nullable = false)
    private String company;
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
