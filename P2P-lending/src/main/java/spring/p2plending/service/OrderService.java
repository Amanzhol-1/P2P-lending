package spring.p2plending.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import spring.p2plending.dto.OrderRequestDTO;
import spring.p2plending.dto.OrderResponseDTO;
import spring.p2plending.enums.OrderStatus;
import spring.p2plending.model.Order;
import spring.p2plending.repository.OrderRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String ORDER_TOPIC = "orders_topic";

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        Order order = Order.builder()
                .accountId(dto.getAccountId())
                .productId(dto.getProductId())
                .price(/* цена, полученная, например, из ProductService */ null) // ВАЖНО
                .status(OrderStatus.PENDING)
                .timestamp(LocalDateTime.now())
                .build();
        Order savedOrder = orderRepository.save(order);
        kafkaTemplate.send(ORDER_TOPIC, "Order created: " + savedOrder.getId());
        return mapToResponseDTO(savedOrder);
    }

    private OrderResponseDTO mapToResponseDTO(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .accountId(order.getAccountId())
                .productId(order.getProductId())
                .price(order.getPrice())
                .status(order.getStatus())
                .timestamp(order.getTimestamp())
                .build();
    }
}

