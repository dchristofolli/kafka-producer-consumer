package br.com.dchristofolli.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@EnableScheduling
@Slf4j
public class KafkaSender {
    private KafkaService service;

    @Scheduled(fixedDelay = 5000)
    public void send() {
        Integer value = service.generator();
        ProducerRecord<Integer, Integer> record = service.payloadTemplate(value);
        service.send(record.topic(), record);
    }
}