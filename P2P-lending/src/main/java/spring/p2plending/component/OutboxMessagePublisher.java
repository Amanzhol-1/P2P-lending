package spring.p2plending.component;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import spring.p2plending.model.OutboxEvent;
import spring.p2plending.repository.OutboxEventRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxMessagePublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 60000) // Каждые 60 сек
    public void publishOutboxEvents() {
        List<OutboxEvent> events = outboxEventRepository.findByProcessedFalse();
        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send(event.getEventType(), String.valueOf(event.getAggregateId()), event.getPayload());
                event.setProcessed(true);
                outboxEventRepository.save(event);
            } catch (Exception e) {
                System.err.println("Ошибка отправки Outbox-события: " + e.getMessage());
            }
        }
    }
}

