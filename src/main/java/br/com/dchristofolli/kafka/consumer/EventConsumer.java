package br.com.dchristofolli.kafka.consumer;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.concurrent.CountDownLatch;

@Slf4j
@Getter
public class EventConsumer {
    private CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(topics = {

    })
    public void entidadeComercialReceiver(@Payload String payload) {
        log.info("received payload='{}'", payload);
        latch.countDown();
    }
}
