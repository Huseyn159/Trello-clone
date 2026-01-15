package az.huseyn.trelloclone.common.exception;


import az.huseyn.trelloclone.board.exception.BoardNotFoundException;
import az.huseyn.trelloclone.common.dto.ErrorResponseDto;
import az.huseyn.trelloclone.user.exception.EmailAlreadyExsistsException;
import az.huseyn.trelloclone.user.exception.PasswordMismatchException;
import az.huseyn.trelloclone.user.exception.UserNotFoundException;
import az.huseyn.trelloclone.user.exception.UsernameTakenException;
import az.huseyn.trelloclone.workspace.exception.AccessDeniedException;
import az.huseyn.trelloclone.workspace.exception.WorkspaceNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({
            WorkspaceNotFoundException.class,
            BoardNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<ErrorResponseDto> handleNotFound(RuntimeException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({
            AccessDeniedException.class
    })
    public ResponseEntity<ErrorResponseDto> handleForbidden(RuntimeException ex) {
        return build(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler({
            EmailAlreadyExsistsException.class,
            UsernameTakenException.class,
            PasswordMismatchException.class
    })
    public ResponseEntity<ErrorResponseDto> handleConflict(RuntimeException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    private ResponseEntity<ErrorResponseDto> build(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new ErrorResponseDto(
                        LocalDateTime.now(),
                        status.value(),
                        status.getReasonPhrase(),
                        message
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        String message = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );
        return new ResponseEntity<>(errorResponseDto,status);
    }

}
