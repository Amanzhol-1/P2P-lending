package spring.p2plending.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.p2plending.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> { }
