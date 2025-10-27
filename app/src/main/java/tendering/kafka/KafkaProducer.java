package tendering.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tendering.dto.TenderDTO;

@Service
public class KafkaProducer {

    private static final String TOPIC = "3pl-tenders";

    @Autowired
    private KafkaTemplate<String, TenderDTO> kafkaTemplate;

    public void broadcastTender(TenderDTO tenderDTO) {
        kafkaTemplate.send(TOPIC, tenderDTO);
        System.out.println("✅ Broadcasted Tender to Kafka: " + tenderDTO.getTenderNo());
    }
}
