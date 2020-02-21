package br.com.dchristofolli.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@EnableScheduling
@Slf4j
public class KafkaApplicationFacade {

    @Scheduled(fixedDelay = 5000)
    public void send() {

    }
}