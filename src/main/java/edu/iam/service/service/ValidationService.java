package edu.iam.service.service;


import edu.iam.service.domain.dto.SignUpRequest;
import edu.iam.service.domain.service.UserService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationService {

    private final UserService userService;

    public void validateUserCredentials(SignUpRequest signUpRequest) {

        validateUsername(signUpRequest.username());
        validateEmail(signUpRequest.email());
        validatePassword(signUpRequest.password(), signUpRequest.confirmPassword());

        log.info("ValidationService | Validation success username:{}, email:{}", signUpRequest.username(), signUpRequest.email());
    }

    private void validateUsername(String username) {

        if (userService.findUserByUsername(username) != null) {
            throw new ValidationException("Username is already in use");
        }
        if (!username.matches("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$")) {
            throw new ValidationException("Invalid username %s, " +
                    "Username must be 5-20 characters long. " +
                    "Only alphanumeric characters, dots (.), underscores (_), and hyphens (-) are allowed. " +
                    "Dots, underscores, and hyphens cannot be the first or last character. " +
                    "Dots, underscores, and hyphens cannot appear consecutively.");
        }

        log.info("ValidationService | Username is valid");
    }

    private void validateEmail(String email) {

        if (userService.findUserByEmail(email) != null) {
            throw new ValidationException("Email is already in use");
        }
        if (!email.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+" +
                ")*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
            throw new ValidationException("Email should be in the format 'your_email@gmail.com'," +
                    " where 'something' can contain any characters and 'domain' should not contain spaces.");
        }

        log.info("ValidationService | Email is valid");
    }

    private void validatePassword(String password, String confirmPassword) {

        if (password.length() < 8) {
            throw new ValidationException("Password length must be at least 8 characters.");
        }
        if (!password.matches(".*\\d.*")) {
            throw new ValidationException("Password must contain at least one digit.");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new ValidationException("Password must contain at least one lowercase letter.");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new ValidationException("Password must contain at least one uppercase letter.");
        }
        if (!password.matches(".*[!@#$%&*].*")) {
            throw new ValidationException("Password must have at least one special character (e.g., @, #, $, %, etc.).");
        }
        if (!confirmPassword.equals(password)) {
            throw new ValidationException("Passwords do not match.");
        }

        log.info("ValidationService | Password is valid");
    }
}
