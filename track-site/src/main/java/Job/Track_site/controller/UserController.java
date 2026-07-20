package Job.Track_site.controller;


import Job.Track_site.dto.LoginDto;
import Job.Track_site.dto.UserDto;
import Job.Track_site.dto.UserResponseDto;
import Job.Track_site.exceptions.InvalidCredentials;
import Job.Track_site.models.User;
import Job.Track_site.service.UserService;
import Job.Track_site.utility.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
public class UserController {


    UserService userService;
    Mapper mapper;

    public UserController(UserService userService, Mapper mapper){
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping("/save")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserDto userDto){
        User savedUser = userService.registerUser(userDto);
        UserResponseDto responseDto = mapper.mapUserToUserResponseDto(savedUser);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        String token = userService.isValidCredentials(loginDto.getEmail(), loginDto.getPassword());
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token){
        String message = userService.verifyEmail(token);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
