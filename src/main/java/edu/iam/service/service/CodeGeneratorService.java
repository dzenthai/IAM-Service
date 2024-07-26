package edu.iam.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;


@Slf4j
@Service
@RequiredArgsConstructor
public class CodeGeneratorService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final int CODE_LENGTH = 6;

    public String generateVerificationCode() {
        var random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    public void saveVerificationCode(String email, String code) {
        log.info("VerifyCodeGenerator | Saving the verification code: email={}", email);
        redisTemplate.opsForValue().set(email, code, Duration.ofMinutes(10));
    }

    public boolean verifyEmailAndCode(String email, String code) {
        log.info("VerifyCodeGenerator | Verification of the code is in progress: email={}", email);
        var storedCode = redisTemplate.opsForValue().get(email);
        return code.equals(storedCode);
    }

    public void deleteVerificationCode(String email) {
        log.warn("VerifyCodeGenerator | Clearing the verification code from the database: email={}", email);
        redisTemplate.delete(email);
    }
}
