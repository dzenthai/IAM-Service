package edu.iam.service.domain.service;

import edu.iam.service.domain.entity.User;
import edu.iam.service.domain.repository.UserRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void saveUser(User user) {
        log.info("UserService | Saving user: {}", user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public User findUserByEmail(String email) {

        log.info("UserService | Find user by email: {}", email);

        User user = null;

        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isPresent()) {
            user = opt.get();
        }

        return user;
    }

    @Transactional
    public User findUserByUsername(String username) {

        log.info("UserService | Find user by username: {}", username);

        User user = null;

        Optional<User> opt = userRepository.findByUsername(username);
        if (opt.isPresent()) {
            user = opt.get();
        }

        return user;
    }
    
}
