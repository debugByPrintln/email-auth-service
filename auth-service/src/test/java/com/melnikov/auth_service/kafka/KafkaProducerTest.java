package com.melnikov.auth_service.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class KafkaProducerTest {

    private final KafkaTemplate<String, String> kafkaTemplate = mock(KafkaTemplate.class);
    private final KafkaProducer kafkaProducer = new KafkaProducer(kafkaTemplate);

    @Test
    void shouldSendEmailAndCodeThroughKafka() {
        String email = "test@example.com";
        String code = "123456";

        kafkaProducer.sendEmail(email, code);

        verify(kafkaTemplate).send(eq("email-topic"), eq(email), eq(code));
    }
}
