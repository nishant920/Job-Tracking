package Job.Track_site.service;

import Job.Track_site.dto.UserDto;
import Job.Track_site.exceptions.InvalidCredentials;
import Job.Track_site.models.User;
import Job.Track_site.repository.UserRepository;
import Job.Track_site.utility.JwtUtility;
import Job.Track_site.utility.Mapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userRepository;
    Mapper mapper;
    JwtUtility jwtUtility;

    public UserService(UserRepository userRepository, Mapper mapper, JwtUtility jwtUtility){
        this.userRepository=userRepository;
        this.mapper=mapper;
        this.jwtUtility=jwtUtility;
    }


    public User registerUser(UserDto userDto){
        User exixtingUser = userRepository.findByEmail(userDto.getEmail());
        if(exixtingUser!=null){
            throw new RuntimeException("User already exists");
        }

        User user =  mapper.mapUserDetailsToUser(userDto);
        user.setVerified(false);

        return userRepository.save(user);
    }

    public String isValidCredentials(String email, String password){
      User user = userRepository.findByEmail(email);

      if(user.getPassword().equals(password)){
          return jwtUtility.generateToken(user.getEmail(), user.getPassword(), user.getRole());
      }
      throw new InvalidCredentials("Email or password is wrong");
    }


}
