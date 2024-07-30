package edu.iam.service.controller;

import edu.iam.service.domain.dto.SignUpRequest;
import edu.iam.service.domain.dto.VerificationRequest;
import edu.iam.service.service.activation.ActivationService;
import edu.iam.service.service.registration.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    private final ActivationService activationService;

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignUpRequest signUpRequest) {

        registrationService.registerUser(signUpRequest);

        log.info("RegistrationController | Registration success, status: {}, username: {}, email: {}.",
                HttpStatus.OK.getReasonPhrase(), signUpRequest.username(), signUpRequest.email());

        return ResponseEntity.ok()
                .body("Registration successful. Check your email - %s for the verification code."
                        .formatted(signUpRequest.email()));
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestBody VerificationRequest verificationRequest) {

        activationService.verifyUser(verificationRequest);

        log.info("RegistrationController | Email verified successfully, status: {}, email: {}.",
                HttpStatus.OK.getReasonPhrase(), verificationRequest.email());

        return ResponseEntity.ok()
                .body("Email verified successfully.");
    }
}
