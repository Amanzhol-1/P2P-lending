package spring.p2plending.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.p2plending.enums.TransactionType;
import spring.p2plending.model.Transaction;
import spring.p2plending.model.User;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromUser(User fromUser);
    List<Transaction> findByToUser(User toUser);
    List<Transaction> findByTransactionType(TransactionType transactionType);
}
