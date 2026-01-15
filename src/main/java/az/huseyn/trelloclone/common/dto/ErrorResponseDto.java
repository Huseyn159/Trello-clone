package az.huseyn.trelloclone.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {


    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;


}
