package edu.iam.service.controller;

import edu.iam.service.domain.dto.SignInRequest;
import edu.iam.service.service.authentication.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public String signInAndGetToken(@RequestBody SignInRequest signInRequest) {

        log.info("AuthController | User with username: {}, is trying to log in.", signInRequest.username());

        return authService.signInAndGetToken(signInRequest);
    }
}
