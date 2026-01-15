package az.huseyn.trelloclone.auth.controller;

import az.huseyn.trelloclone.auth.dto.LoginRequestDto;
import az.huseyn.trelloclone.auth.dto.LoginResponseDto;
import az.huseyn.trelloclone.user.entity.UserEntity;
import az.huseyn.trelloclone.user.exception.UserNotFoundException;
import az.huseyn.trelloclone.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name="Login",description = "User Login")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto request
    ) {
        UserEntity user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Invalid credentials"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new UserNotFoundException("Invalid credentials");
        }

        return ResponseEntity.ok(
                new LoginResponseDto(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail()
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }
}
