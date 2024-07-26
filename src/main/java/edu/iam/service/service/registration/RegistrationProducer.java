package edu.iam.service.service.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.iam.service.domain.dto.SignUpRequest;
import edu.iam.service.service.CodeGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationProducer {

    @Value("${io.kafka.registration.topic}")
    private String REGISTRATION_TOPIC;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final CodeGeneratorService codeGeneratorService;

    public void registerUser(SignUpRequest signUpRequest) {

        log.info("RegistrationProducer | Sending registration code to email: {}", signUpRequest.email());

        try {
            var email = signUpRequest.email();
            var code = codeGeneratorService.generateVerificationCode();
            codeGeneratorService.saveVerificationCode(email, code);

            Map<String, String> record = new HashMap<>();
            record.put("email", email);
            record.put("code", code);

            ObjectMapper objectMapper = new ObjectMapper();
            String recordString = objectMapper.writeValueAsString(record);

            kafkaTemplate.send(REGISTRATION_TOPIC, recordString);

        } catch (Exception e) {
            log.error("RegistrationProducer | Error sending registration code to email: {}", signUpRequest.email(), e);
        }
    }
}
