package edu.iam.service.service.authentication;

import edu.iam.service.domain.dto.SignInRequest;
import edu.iam.service.domain.exception.AuthException;
import edu.iam.service.service.CustomUserDetailsService;
import edu.iam.service.utils.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;

    public String authUser(SignInRequest signInRequest) {

        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequest.username(), signInRequest.password()));

            if (!authentication.isAuthenticated()) {
                throw new AuthException("AuthService | Invalid credentials");
            }

            log.info("AuthService | User with username: {}, successfully authenticated", signInRequest.username());
            return jwtService.generateToken((CustomUserDetails) userDetailsService.loadUserByUsername(signInRequest.username()));

        } catch (AuthenticationException ex) {
            throw new AuthException("AuthService | Invalid credentials");

        }
    }
}
