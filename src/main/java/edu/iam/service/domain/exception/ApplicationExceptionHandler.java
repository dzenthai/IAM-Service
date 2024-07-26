package edu.iam.service.domain.exception;

import edu.iam.service.domain.dto.ApplicationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Object> exception(AuthException ex) {
        ApplicationException exception = new ApplicationException
                (ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        log.error("Auth exception | exception: {}, status: {}", ex.getMessage(), exception.getStatus());
        return new ResponseEntity<>(exception, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApplicationException> exception(AuthenticationException ex) {
        ApplicationException exception = new ApplicationException(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        log.error("Authentication exception | exception: {}, status: {}", ex.getMessage(), exception.getStatus());
        return new ResponseEntity<>(exception, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApplicationException> exception(AccessDeniedException ex) {
        ApplicationException exception = new ApplicationException(ex.getMessage(), HttpStatus.FORBIDDEN.value());
        log.error("Access exception | exception: {}, status: {}", ex.getMessage(), exception.getStatus());
        return new ResponseEntity<>(exception, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApplicationException> exception(ValidationException ex) {
        ApplicationException exception = new ApplicationException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        log.error("Validation exception | exception: {}, status: {}", ex.getMessage(), exception.getStatus());
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApplicationException> exception(RuntimeException ex) {
        ApplicationException exception = new ApplicationException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        log.error("Generic exception | exception: {}, status: {}", ex.getMessage(), exception.getStatus());
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }
}
