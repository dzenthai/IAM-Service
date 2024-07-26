package edu.iam.service.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Component
public class AttemptTracker {

    private static final int MAX_ATTEMPTS = 3;
    private static final long BLOCK_TIME_MS = 30000;

    private final Map<String, AttemptInfo> attemptsMap = new ConcurrentHashMap<>();

    private static class AttemptInfo {
        int attempts;
        long lastAttemptTime;

        AttemptInfo(int attempts, long lastAttemptTime) {
            this.attempts = attempts;
            this.lastAttemptTime = lastAttemptTime;
        }
    }

    public synchronized void registerAttempt(String email) {
        log.info("AttemptTracker | Registering attempt {}", email);
        AttemptInfo attemptInfo = attemptsMap.getOrDefault(email, new AttemptInfo(0, System.currentTimeMillis()));
        attemptInfo.attempts++;
        attemptInfo.lastAttemptTime = System.currentTimeMillis();
        attemptsMap.put(email, attemptInfo);
    }

    public synchronized void resetAttempts(String email) {
        log.info("AttemptTracker | Resetting attempt {}", email);
        attemptsMap.remove(email);
    }

    public synchronized boolean hasExceededMaxAttempts(String email) {
        log.info("AttemptTracker | User's attempt count exceeded: email: {}", email);
        AttemptInfo attemptInfo = attemptsMap.get(email);
        if (attemptInfo == null) {
            return false;
        }

        if (attemptInfo.attempts >= MAX_ATTEMPTS) {
            long timeSinceLastAttempt = System.currentTimeMillis() - attemptInfo.lastAttemptTime;
            if (timeSinceLastAttempt >= BLOCK_TIME_MS) {
                resetAttempts(email);
                return false;
            }
            return true;
        }
        return false;
    }

    public synchronized long getTimeUntilNextAttempt(String email) {
        AttemptInfo attemptInfo = attemptsMap.get(email);
        if (attemptInfo == null) {
            return 0;
        }

        long timeSinceLastAttempt = System.currentTimeMillis() - attemptInfo.lastAttemptTime;
        return Math.max(BLOCK_TIME_MS - timeSinceLastAttempt, 0);
    }
}
