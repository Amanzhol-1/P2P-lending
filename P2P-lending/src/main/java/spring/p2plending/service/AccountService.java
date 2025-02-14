package spring.p2plending.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.p2plending.dto.AccountResponseDTO;
import spring.p2plending.exception.EntityNotFoundException;
import spring.p2plending.model.Account;
import spring.p2plending.repository.AccountRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Cacheable(value = "accounts", key = "#id")
    public AccountResponseDTO getAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        return mapToResponseDTO(account);
    }

    @Transactional
    @CachePut(value = "accounts", key = "#id")
    public AccountResponseDTO updateBalance(Long id, BigDecimal newBalance) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Account not found"));
        account.setBalance(newBalance);
        Account updated = accountRepository.save(account);
        return mapToResponseDTO(updated);
    }

    @Transactional(readOnly = true)
    public Account getAccountEntity(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
    }

    @Transactional
    public Account saveAccountEntity(Account account) {
        return accountRepository.save(account);
    }

    private AccountResponseDTO mapToResponseDTO(Account account) {
        return AccountResponseDTO.builder()
                .id(account.getId())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .status(account.getStatus())
                .build();
    }
}

