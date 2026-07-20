package Job.Track_site.controller;

import Job.Track_site.dto.LoginDto;
import Job.Track_site.dto.UserDto;
import Job.Track_site.dto.UserResponseDto;
import Job.Track_site.models.User;
import Job.Track_site.service.UserService;
import Job.Track_site.utility.Mapper;
import jakarta.validation.Valid;
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

    // @Valid triggers Spring's Bean Validation on incoming UserDto before endpoint execution.
    // If validation fails, MethodArgumentNotValidException is thrown and caught by GlobalExceptionHandler.
    @PostMapping("/save")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserDto userDto){
        User savedUser = userService.registerUser(userDto);
        UserResponseDto responseDto = mapper.mapUserToUserResponseDto(savedUser);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // @Valid enforces validation constraints on LoginDto (email format, non-blank fields)
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto){
        String token = userService.isValidCredentials(loginDto.getEmail(), loginDto.getPassword());
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token){
        String message = userService.verifyEmail(token);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
