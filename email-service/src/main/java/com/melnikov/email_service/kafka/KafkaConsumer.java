package com.melnikov.email_service.kafka;

import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

/**
 * Сервис для обработки сообщений из Kafka.
 * Слушает топик Kafka и обрабатывает входящие сообщения.
 *
 * @author Мельников Никита
 */
@Service
@Log4j2
public class KafkaConsumer {

    /**
     * Обрабатывает сообщения из Kafka.
     * Логирует полученный email и код подтверждения.
     *
     * @param email адрес электронной почты, полученный из Kafka
     * @param code  код подтверждения, полученный из Kafka
     */
    @KafkaListener(topics = "email-topic", groupId = "email-group")
    public void listen(@Header(KafkaHeaders.RECEIVED_KEY) String email, String code) {
        log.info("Received a message for email: {}", email);
        log.info("Verification code: {}", code);
    }
}
