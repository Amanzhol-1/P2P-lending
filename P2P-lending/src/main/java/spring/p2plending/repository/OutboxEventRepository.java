package spring.p2plending.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.p2plending.model.OutboxEvent;
import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {
    List<OutboxEvent> findByProcessedFalse();
}


