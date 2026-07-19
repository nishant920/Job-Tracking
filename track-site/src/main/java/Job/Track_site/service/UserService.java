package Job.Track_site.service;

import Job.Track_site.dto.UserDto;
import Job.Track_site.enums.Role;
import Job.Track_site.exceptions.InvalidCredentials;
import Job.Track_site.models.User;
import Job.Track_site.repository.UserRepository;
import Job.Track_site.repository.VerificationRepository;
import Job.Track_site.utility.JwtUtility;
import Job.Track_site.utility.Mapper;
import Job.Track_site.verification.VerificationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class UserService {
    UserRepository userRepository;
    Mapper mapper;
    JwtUtility jwtUtility;
    VerificationRepository verificationRepository;
    MailService mailService;

    PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, Mapper mapper,
                       JwtUtility jwtUtility, VerificationRepository verificationRepository, MailService mailService,
                       PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.mapper=mapper;
        this.jwtUtility=jwtUtility;
        this.verificationRepository=verificationRepository;
        this.mailService=mailService;
        this.passwordEncoder=passwordEncoder;
    }


    public User registerUser(UserDto userDto){
        User exixtingUser = userRepository.findByEmail(userDto.getEmail());
        if(exixtingUser!=null){
            throw new RuntimeException("User already exists");
        }

        User user =  mapper.mapUserDetailsToUser(userDto);
        //set verify false as the email hasn't verified yet
        user.setVerified(false);
        user.setRole(Role.AppUser);

        user.setPassword(passwordEncoder.encode(userDto.getPassword())); //encoding the password for security reason

        User savedUser = userRepository.save(user);
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        log.info("Token generated on registration of user {} ", token);
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        verificationToken.setUser(savedUser);

        verificationRepository.save(verificationToken);
        mailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getName(), token);

        return savedUser;
    }

    public String verifyEmail(String token){
        VerificationToken verificationToken = verificationRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        //if the deadline is earlier than "right now," it means the token is officially expired
        if(verificationToken.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Verification token expired");
        }

        User user = verificationToken.getUser();
        user.setVerified(true);
        userRepository.save(user);
        verificationRepository.delete(verificationToken);
        return "Email verified successfully";
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public String isValidCredentials(String email, String password){
      User user = userRepository.findByEmail(email);

        if(user == null || !passwordEncoder.matches(password, user.getPassword())){
            throw new InvalidCredentials("Invalid credentials");
        }

        if(!user.isVerified()){
          throw new RuntimeException("Please verify your email first"); //throw the exception in case a user tries to log in without verification
        }

      return jwtUtility.generateToken(user.getEmail(), user.getRole());

    }

 // side note -> matches() extracts the salt and cost factor from the stored hash automatically, hashes the incoming raw password the same way, and compares the two resulting hashes. You never touch the plaintext password again after registration
}
