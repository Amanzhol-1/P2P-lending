package spring.p2plending.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import spring.p2plending.dto.TransactionRequestDTO;
import spring.p2plending.dto.TransactionResponseDTO;
import spring.p2plending.enums.AccountStatus;
import spring.p2plending.enums.TransactionType;
import spring.p2plending.exception.AccountBlockedException;
import spring.p2plending.exception.InsufficientFundsException;
import spring.p2plending.model.Transaction;
import spring.p2plending.repository.TransactionRepository;
import spring.p2plending.model.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TRANSACTION_TOPIC = "transactions_topic";

    @Transactional
    public TransactionResponseDTO createTransaction(TransactionRequestDTO transactionRequest) {

        Account account = accountService.getAccountEntity(transactionRequest.getAccountId());

        if (account.getStatus() == AccountStatus.BLOCKED) {
            throw new AccountBlockedException("Account is blocked, can't perform transactions");
        }

        if (transactionRequest.getType() == null) transactionRequest.setType(TransactionType.DEBIT);

        BigDecimal amount = transactionRequest.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive");
        }

        if (transactionRequest.getType() == TransactionType.DEBIT) {
            if (account.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException("Insufficient funds");
            }
            account.setBalance(account.getBalance().subtract(amount));
        } else {
            account.setBalance(account.getBalance().add(amount));
        }

        accountService.saveAccountEntity(account);

        Transaction tx = Transaction.builder()
                .accountId(transactionRequest.getAccountId())
                .type(transactionRequest.getType())
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .description(transactionRequest.getDescription())
                .build();

        Transaction saved = transactionRepository.save(tx);

        kafkaTemplate.send(TRANSACTION_TOPIC, "Transaction created: " + saved.getId());

        return mapToResponseDTO(saved);
    }

    private TransactionResponseDTO mapToResponseDTO(Transaction tx) {
        return TransactionResponseDTO.builder()
                .id(tx.getId())
                .accountId(tx.getAccountId())
                .type(tx.getType())
                .amount(tx.getAmount())
                .timestamp(tx.getTimestamp())
                .description(tx.getDescription())
                .build();
    }
}
