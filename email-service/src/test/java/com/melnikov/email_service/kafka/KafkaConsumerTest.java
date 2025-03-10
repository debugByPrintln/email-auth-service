package com.melnikov.email_service.kafka;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@DirtiesContext
public class KafkaConsumerTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @MockitoSpyBean
    private KafkaConsumer kafkaConsumer;

    @Test
    void shouldCallListenMethod() {
        String topic = "email-topic";
        String email = "test@example.com";
        String code = "123456";

        kafkaTemplate.send(topic, email, code);

        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);

        verify(kafkaConsumer, timeout(10000).times(1)).listen(emailCaptor.capture(), codeCaptor.capture());

        assertThat(emailCaptor.getValue()).isEqualTo(email);
        assertThat(codeCaptor.getValue()).isEqualTo(code);
    }

    @Test
    void contextLoads() {

    }
}
