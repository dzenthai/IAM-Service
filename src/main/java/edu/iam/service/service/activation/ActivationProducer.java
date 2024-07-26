package edu.iam.service.service.activation;

import edu.iam.service.domain.dto.VerificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class ActivationProducer {

    @Value("${io.kafka.activation.topic}")
    private String ACTIVATION_TOPIC;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void activateUser(VerificationRequest verificationRequest) {

        log.info("ActivationProducer | Account activated for email: {}", verificationRequest.email());

        try {

            var email = verificationRequest.email();

            kafkaTemplate.send(ACTIVATION_TOPIC, email);

        } catch (Exception e) {
            log.error("ActivationProducer | Error sending registration code to email: {}", verificationRequest.email(), e);
        }
    }
}
