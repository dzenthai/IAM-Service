package edu.iam.service.service;

import edu.iam.service.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class TempUserService {

    private final ConcurrentHashMap<String, User> tempUsers = new ConcurrentHashMap<>();


    public void saveTempUser(String email, User user) {
        log.info("Saving temporary information about user: email={}", email);
        tempUsers.put(email, user);
    }

    public User getTempUser(String email) {
        log.info("Getting temporary information about user: email={}", email);
        return tempUsers.get(email);
    }

    public void removeTempUser(String email) {
        log.warn("Removing temporary information about user: email={}", email);
        tempUsers.remove(email);
    }
}
