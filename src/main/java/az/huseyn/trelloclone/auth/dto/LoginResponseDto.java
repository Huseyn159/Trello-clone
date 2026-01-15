package az.huseyn.trelloclone.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private Long id;
    private String username;
    private String email;
}
