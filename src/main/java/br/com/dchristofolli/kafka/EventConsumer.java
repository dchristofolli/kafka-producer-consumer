package br.com.dchristofolli.kafka;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.concurrent.CountDownLatch;

@Slf4j
@Data
public class EventConsumer {
    private CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(topics = "My_Kafka_Topic")
    public void kafkaConsumer(@Payload String payload) {
        log.info("received payload='{}'", payload);
        latch.countDown();
    }
}
