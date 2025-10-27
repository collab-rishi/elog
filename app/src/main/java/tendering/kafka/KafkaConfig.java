package tendering.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import tendering.dto.TenderDTO;

import java.util.HashMap;
import java.util.Map;

@Configuration // Marks this class as a Spring configuration class
public class KafkaConfig {

    // This bean defines the producer factory – the component responsible for creating Kafka producers.
    @Bean
    public ProducerFactory<String, TenderDTO> producerFactory() {
        // Configuration map for Kafka Producer settings
        Map<String, Object> config = new HashMap<>();
        
        // Specify the Kafka server (broker) address
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");

        // Set the serializer for the message key (String type here)
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // Set the serializer for the message value (we are sending TenderDTO object as JSON)
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // Create and return a producer factory with the above config
        return new DefaultKafkaProducerFactory<>(config);
    }

    // KafkaTemplate is used to send messages to Kafka topics
    @Bean
    public KafkaTemplate<String, TenderDTO> kafkaTemplate() {
        // Create a KafkaTemplate using the producerFactory defined above
        return new KafkaTemplate<>(producerFactory());
    }
}
