package br.com.dchristofolli.kafka.producer;


import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class EventProducer {
    private KafkaTemplate<String, Object> kafkaTemplate;
    private Gson gson;

    public void send(String topic, ProducerRecord<String, Object> producerRecord) {
        kafkaTemplate.send(topic, gson.toJson(producerRecord));
    }
}
