package az.huseyn.trelloclone.user.controller;

import az.huseyn.trelloclone.user.dto.UserRegisterRequestDto;
import az.huseyn.trelloclone.user.dto.UserRegisterResponseDto;
import az.huseyn.trelloclone.user.entity.UserEntity;
import az.huseyn.trelloclone.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<UserRegisterResponseDto> registerUser(@Valid @RequestBody  UserRegisterRequestDto userRegisterRequestDto) {
        UserRegisterResponseDto user = userService.registerUser(userRegisterRequestDto) ;

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(user);
    }



}
