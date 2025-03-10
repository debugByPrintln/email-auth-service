package com.melnikov.auth_service.kafka;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Сервис для отправки сообщений через Kafka.
 * Отправляет коды подтверждения на указанный email.
 *
 * @author Мельников Никита
 */
@Service
@Log4j2
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Конструктор для внедрения зависимости KafkaTemplate.
     *
     * @param kafkaTemplate шаблон для работы с Kafka
     */
    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Отправляет код подтверждения для указанного email через Kafka.
     *
     * @param email адрес электронной почты
     * @param code  код подтверждения
     */
    public void sendEmail(String email, String code) {
        log.info("Sending code for email: {} through Kafka", email);
        kafkaTemplate.send("email-topic", email, code);
    }
}
