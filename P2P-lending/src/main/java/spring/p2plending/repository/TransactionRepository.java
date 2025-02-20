package spring.p2plending.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.p2plending.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> { }

