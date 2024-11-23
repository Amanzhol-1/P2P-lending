package spring.p2plending.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.p2plending.model.Loan;
import spring.p2plending.model.Payment;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByLoan(Loan loan);
    List<Payment> findByIsPaidFalseAndPaymentDateBefore(LocalDateTime dateTime);
    List<Payment> findByIsPaidFalseAndPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
