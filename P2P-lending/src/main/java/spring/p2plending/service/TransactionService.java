package spring.p2plending.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.p2plending.enums.TransactionType;
import spring.p2plending.model.*;
import spring.p2plending.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final LogService logService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, UserService userService, LogService logService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.logService = logService;
    }

    public Transaction createTransaction(Loan loan, User fromUser, User toUser, BigDecimal amount, TransactionType type) {
        userService.updateUserBalance(fromUser, amount.negate());
        userService.updateUserBalance(toUser, amount);

        Transaction transaction = Transaction.builder()
                .loan(loan)
                .fromUser(fromUser)
                .toUser(toUser)
                .amount(amount)
                .transactionDate(LocalDateTime.now())
                .transactionType(type)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        logService.log("INFO", "TransactionService", "Создана транзакция: ID=" + savedTransaction.getId() + ", Тип=" + type, Thread.currentThread().getName());

        return savedTransaction;
    }
}
