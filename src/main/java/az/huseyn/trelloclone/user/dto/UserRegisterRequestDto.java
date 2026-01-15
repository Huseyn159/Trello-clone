package az.huseyn.trelloclone.user.dto;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequestDto {

    @NotBlank(message = "Username must not be blank")
    @Size(min = 3,message = "Username must be at least 3 characters long")
    private String username;


    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "Password must contain at least one letter and one number"
    )
    private String password;

    @NotBlank(message = "Please re-enter password")

    private String confirmPassword;

}
