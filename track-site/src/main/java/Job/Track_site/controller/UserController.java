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
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto){

      try {
          User savedUser = userService.registerUser(userDto);
          UserResponseDto responseDto = mapper.mapUserToUserResponseDto(savedUser);
          return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
      } catch (RuntimeException e) {
          return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        try{
            String token = userService.isValidCredentials(loginDto.getEmail(), loginDto.getPassword());
            return new ResponseEntity<>(token, HttpStatus.OK);
        }catch (InvalidCredentials e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String token){
        try{
            String message = userService.verifyEmail(token);
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch(RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
