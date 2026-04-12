package Job.Track_site.controller;


import Job.Track_site.dto.UserDto;
import Job.Track_site.models.User;
import Job.Track_site.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
public class UserController {


    UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto){

      try {
          User Saveduser = userService.registerUser(userDto);
          return new ResponseEntity<>(Saveduser, HttpStatus.CREATED);
      } catch (RuntimeException e) {
          return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
    }
}
