package br.com.dchristofolli.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import java.util.Random;

@AllArgsConstructor
@Service
@Slf4j
public class KafkaService {
    private EventProducer eventProducer;
    private Integer index = 1;

    public Integer generator() {
        return new Random().nextInt(100);
    }

    public void send(String topic, ProducerRecord<Integer, Integer> producerRecord) {
        index += 1;
        eventProducer.send(topic, producerRecord);
    }

    public ProducerRecord<Integer, Integer> payloadTemplate(Integer number) {
        return new ProducerRecord<>("My_Kafka_Topic",
                1,
                null,
                index,
                number);
    }
}
