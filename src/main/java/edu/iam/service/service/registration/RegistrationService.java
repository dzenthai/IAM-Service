package edu.iam.service.service.registration;

import edu.iam.service.domain.dto.SignUpRequest;
import edu.iam.service.domain.entity.Role;
import edu.iam.service.domain.entity.User;
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

    public void registerUser(SignUpRequest signUpRequest) {

        log.info("RegistrationService | Register user: {}, email: {}", signUpRequest.username(), signUpRequest.email());

        validateUser(signUpRequest);

        tempUserService.saveTempUser(signUpRequest.email(),
                User.builder()
                        .email(signUpRequest.email())
                        .username(signUpRequest.username())
                        .password(signUpRequest.password())
                        .role(Role.ROLE_USER)
                        .build());
    }

    private void validateUser(SignUpRequest signUpRequest) {

        log.info("RegistrationService | Validate user: {}, email: {}", signUpRequest.username(), signUpRequest.email());

        if (attemptTracker.hasExceededMaxAttempts(signUpRequest.email())) {
            long timeUntilNextAttempt = attemptTracker.getTimeUntilNextAttempt(signUpRequest.email());
            throw new ValidationException("Attempt count exceeded. Try again in "
                    + (timeUntilNextAttempt / 1000) + " seconds.");
        }

        try {
            validationService.validateUserCredentials(signUpRequest);

            attemptTracker.resetAttempts(signUpRequest.email());

            registrationProducer.registerUser(signUpRequest);
        } catch (ValidationException exception) {
            attemptTracker.registerAttempt(signUpRequest.email());
            throw exception;
        }
    }
}
