package spring.p2plending.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import spring.p2plending.model.OutboxEvent;
import spring.p2plending.repository.OutboxEventRepository;
import spring.p2plending.service.LogService;

import java.util.List;

@Component
public class OutboxMessagePublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final LogService logService;

    @Autowired
    public OutboxMessagePublisher(OutboxEventRepository outboxEventRepository,
                                  KafkaTemplate<String, String> kafkaTemplate,
                                  LogService logService) {
        this.outboxEventRepository = outboxEventRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.logService = logService;
    }

    @Scheduled(fixedDelay = 5000) // Каждые 5 секунд
    public void publishOutboxMessages() {
        List<OutboxEvent> events = outboxEventRepository.findByProcessedFalse();
        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send(event.getEventType(), String.valueOf(event.getAggregateId()), event.getPayload());
                event.setProcessed(true);
                outboxEventRepository.save(event);

                logService.log("INFO", "OutboxMessagePublisher", "Сообщение отправлено: " + event.getEventType()
                        + ", ID=" + event.getId(), Thread.currentThread().getName());

            } catch (Exception e) {
                logService.log("ERROR", "OutboxMessagePublisher", "Ошибка отправки сообщения: " + e.getMessage()
                        + ", ID=" + event.getId(), Thread.currentThread().getName());
            }
        }
    }
}
