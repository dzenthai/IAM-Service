package edu.iam.service.service.activation;

import edu.iam.service.domain.dto.VerificationRequest;
import edu.iam.service.domain.entity.User;
import edu.iam.service.domain.service.UserService;
import edu.iam.service.service.CodeGeneratorService;
import edu.iam.service.service.TempUserService;
import edu.iam.service.utils.AttemptTracker;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class ActivationService {

    private final UserService userService;

    private final ActivationProducer activationProducer;

    private final AttemptTracker attemptTracker;

    private final TempUserService tempUserService;

    private final CodeGeneratorService codeGeneratorService;

    public void verifyUser(VerificationRequest verificationRequest) {

        var email = verificationRequest.email();

        var code = verificationRequest.code();

        if (attemptTracker.hasExceededMaxAttempts(email)) {
            long timeUntilNextAttempt = attemptTracker.getTimeUntilNextAttempt(email);
            throw new ValidationException("Attempt count exceeded. Try again in " + (timeUntilNextAttempt / 2000) + " seconds.");
        }
        if (userService.findUserByEmail(email) != null) {
            attemptTracker.registerAttempt(email);
            throw new ValidationException("Email is already in use");
        }
        if (!codeGeneratorService.verifyEmailAndCode(email, code)) {
            attemptTracker.registerAttempt(email);
            throw new ValidationException("Verification code is incorrect");

        }

        attemptTracker.registerAttempt(email);

        User user = tempUserService.getTempUser(email);

        userService.saveUser(user);

        log.info("RegistrationService | User with email: {}, was registered successfully.", verificationRequest.email());

        activationProducer.activateUser(verificationRequest);

        tempUserService.removeTempUser(email);

        codeGeneratorService.deleteVerificationCode(email);

    }
}
