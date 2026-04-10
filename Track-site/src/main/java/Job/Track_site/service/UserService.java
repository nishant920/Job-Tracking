package Job.Track_site.service;

import Job.Track_site.dto.UserDto;
import Job.Track_site.models.User;
import Job.Track_site.repository.UserRepository;
import Job.Track_site.utility.Mapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userRepository;
    Mapper mapper;

    public UserService(UserRepository userRepository, Mapper mapper){
        this.userRepository=userRepository;
        this.mapper=mapper;
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


}
