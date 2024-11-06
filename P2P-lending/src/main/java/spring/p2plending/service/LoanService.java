package spring.p2plending.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.p2plending.enums.LoanStatus;
import spring.p2plending.enums.OfferStatus;
import spring.p2plending.enums.RequestStatus;
import spring.p2plending.enums.TransactionType;
import spring.p2plending.model.*;
import spring.p2plending.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BorrowingRequestRepository borrowingRequestRepository;
    private final LenderOfferRepository lenderOfferRepository;
    private final TransactionService transactionService;
    private final LogService logService;
    private final PaymentRepository paymentRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository,
                       BorrowingRequestRepository borrowingRequestRepository,
                       LenderOfferRepository lenderOfferRepository,
                       TransactionService transactionService,
                       LogService logService,
                       PaymentRepository paymentRepository) {
        this.loanRepository = loanRepository;
        this.borrowingRequestRepository = borrowingRequestRepository;
        this.lenderOfferRepository = lenderOfferRepository;
        this.transactionService = transactionService;
        this.logService = logService;
        this.paymentRepository = paymentRepository;
    }

    public Loan createLoan(BorrowingRequest request, LendingOffer offer) {
        // Обновляем статус
        request.setStatus(RequestStatus.MATCHED);
        request.setMatchedOffer(offer);
        borrowingRequestRepository.save(request);

        offer.setStatus(OfferStatus.ACCEPTED);
        lenderOfferRepository.save(offer);

        // Создаем кредит
        Loan loan = new Loan();
        loan.setBorrower(request.getBorrower());
        loan.setLender(offer.getLender());
        loan.setAmount(offer.getAmount());
        loan.setInterestRate(offer.getInterestRate());
        loan.setTermInMonths(offer.getTermInMonths());
        loan.setStartDate(LocalDateTime.now());
        loan.setEndDate(LocalDateTime.now().plusMonths(offer.getTermInMonths()));
        loan.setStatus(LoanStatus.ACTIVE);

        Loan savedLoan = loanRepository.save(loan);

        transactionService.createTransaction(
                savedLoan,
                offer.getLender(),
                request.getBorrower(),
                offer.getAmount(),
                TransactionType.LOAN_ISSUED
        );

        logService.log("INFO", "LoanService", "Создан новый кредит: ID=" + savedLoan.getId(), Thread.currentThread().getName());

        return savedLoan;
    }

    public void updateLoanStatus(Loan loan) {
        List<Payment> payments = paymentRepository.findByLoan(loan);

        boolean allPaid = payments.stream().allMatch(Payment::getIsPaid);

        if (allPaid) {
            loan.setStatus(LoanStatus.COMPLETED);
            loanRepository.save(loan);

            logService.log("INFO", "LoanService", "Кредит ID=" + loan.getId() + " завершен.", Thread.currentThread().getName());
        } else {
            // Проверяем просрочку
            LocalDateTime now = LocalDateTime.now();
            boolean hasOverdue = payments.stream()
                    .anyMatch(p -> !p.getIsPaid() && p.getPaymentDate().isBefore(now));

            if (hasOverdue) {
                loan.setStatus(LoanStatus.DEFAULTED);
                loanRepository.save(loan);

                logService.log("WARN", "LoanService", "Кредит ID=" + loan.getId() + " имеет просроченные платежи.", Thread.currentThread().getName());
            }
        }
    }
}
