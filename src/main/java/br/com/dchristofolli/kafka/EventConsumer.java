package br.com.dchristofolli.kafka;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.concurrent.CountDownLatch;

@Slf4j
@Getter
public class EventConsumer {
    private CountDownLatch latch = new CountDownLatch(1);

//    @KafkaListener(topics = "My_Kafka_Topic")
//    public void kafkaConsumer(@Payload Integer payload) {
//        log.info("received payload='{}'", payload);
//        latch.countDown();
//    }
}
