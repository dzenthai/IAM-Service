package edu.iam.service.service.registration;

import edu.iam.service.domain.dto.SignUpRequest;
import edu.iam.service.domain.entity.Role;
import edu.iam.service.domain.entity.User;
import edu.iam.service.domain.service.UserService;
import edu.iam.service.service.TempUserService;
import edu.iam.service.utils.AttemptTracker;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserService userService;

    private final RegistrationProducer registrationProducer;

    private final AttemptTracker attemptTracker;

    private final TempUserService tempUserService;

    public void validateAndRegisterUser(SignUpRequest signUpRequest) {

        log.info("RegistrationService | Validating user: {}, email: {}", signUpRequest.username(), signUpRequest.email());

        var email = signUpRequest.email();

        var username = signUpRequest.username();

        var password = signUpRequest.password();

        var confirmPassword = signUpRequest.confirmPassword();

        if (attemptTracker.hasExceededMaxAttempts(email)) {
            long timeUntilNextAttempt = attemptTracker.getTimeUntilNextAttempt(email);
            throw new ValidationException("Attempt count exceeded. Try again in "
                    + (timeUntilNextAttempt / 1000) + " seconds.");
        }
        if (userService.findUserByUsername(username) != null) {
            attemptTracker.registerAttempt(email);
            throw new ValidationException("Username is already in use");
        }
        if (userService.findUserByEmail(email) != null) {
            attemptTracker.registerAttempt(email);
            throw new ValidationException("Email is already in use");
        }
        if (!signUpRequest.username().matches("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$")) {
            attemptTracker.registerAttempt(email);
            throw new ValidationException("Invalid username %s, " +
                    "Username must be 5-20 characters long. " +
                    "Only alphanumeric characters, dots (.), underscores (_), and hyphens (-) are allowed. " +
                    "Dots, underscores, and hyphens cannot be the first or last character. " +
                    "Dots, underscores, and hyphens cannot appear consecutively."
            );
        }
        if (!email.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+" +
                ")*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
            attemptTracker.registerAttempt(email);
            throw new ValidationException("Email should be in the format 'your_email@gmail.com'," +
                    " where 'something' can contain any characters and 'domain' should not contain spaces.");
        }
        if (password.length() < 8) {
            attemptTracker.registerAttempt(email);
            throw new ValidationException("Password length must be at least 8 characters.");
        }
        if (!password.matches(".*\\d.*")) {
            attemptTracker.registerAttempt(email);
            throw new ValidationException("Password must contain at least one digit.");
        }
        if (!password.matches(".*[a-z].*")) {
            attemptTracker.registerAttempt(email);
            throw new ValidationException("Password must contain at least one lowercase letter.");
        }
        if (!password.matches(".*[A-Z].*")) {
            attemptTracker.registerAttempt(email);
            throw new ValidationException("Password must contain at least one uppercase letter.");
        }
        if (!password.matches(".*[!@#$%&*].*")) {
            attemptTracker.registerAttempt(email);
            throw new ValidationException("Password must have at least one special character (e.g., @, #, $, %, etc.).");
        }
        if (!confirmPassword.equals(password)) {
            attemptTracker.registerAttempt(email);
            throw new ValidationException("Passwords do not match.");
        }

        attemptTracker.registerAttempt(email);

        registrationProducer.registerUser(signUpRequest);

        tempUserService.saveTempUser(signUpRequest.email(),
                User.builder()
                        .email(email)
                        .username(username)
                        .password(password)
                        .role(Role.ROLE_USER)
                        .build());

    }
}
