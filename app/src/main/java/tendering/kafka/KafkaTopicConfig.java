package tendering.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration // Marks this class as a configuration for Spring
public class KafkaTopicConfig {

    // 🔵 Topic 1: Used to broadcast newly created tenders by 3PL
    @Bean
    public NewTopic tenderTopic() {
        return TopicBuilder.name("new-tenders") // Topic name: new-tenders
                .partitions(3)                 // Number of partitions (parallelism)
                .replicas(1)                   // Number of replicas (for fault tolerance)
                .build();                      // Builds and registers the topic
    }

  
}
