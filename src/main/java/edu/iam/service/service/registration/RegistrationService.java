package edu.iam.service.service.registration;

import edu.iam.service.domain.dto.SignUpRequest;
import edu.iam.service.domain.entity.Role;
import edu.iam.service.domain.entity.User;
import edu.iam.service.domain.service.UserService;
import edu.iam.service.service.TempUserService;
import edu.iam.service.service.ValidationService;
import edu.iam.service.utils.AttemptTracker;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationProducer registrationProducer;

    private final AttemptTracker attemptTracker;

    private final ValidationService validationService;

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

        try {

            validationService.emailValidation(email);

            validationService.usernameValidation(username);

            validationService.passwordValidation(password, confirmPassword);

            attemptTracker.resetAttempts(email);

            registrationProducer.registerUser(signUpRequest);

            tempUserService.saveTempUser(signUpRequest.email(),
                    User.builder()
                            .email(email)
                            .username(username)
                            .password(password)
                            .role(Role.ROLE_USER)
                            .build());

        } catch (ValidationException exception) {
            attemptTracker.registerAttempt(email);
            throw exception;
        }
    }
}
