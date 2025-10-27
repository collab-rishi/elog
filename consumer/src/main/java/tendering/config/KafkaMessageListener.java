package tendering.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tendering.dto.TenderDTO;

@Service
public class KafkaMessageListener {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessageListener.class);

    @KafkaListener(
        topics = {"3pl-tenders"},
        groupId = "tender-lsp-group",
        containerFactory = "tenderKafkaListenerContainerFactory"
    )
    public void listenTenderEvents(TenderDTO tenderDTO) {
        log.info("Consumed TenderDTO event for lsp1 : {}", tenderDTO);
        
        
    }
    
    
    
    
}
