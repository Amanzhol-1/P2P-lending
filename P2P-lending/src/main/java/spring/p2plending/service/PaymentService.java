package spring.p2plending.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.p2plending.enums.TransactionType;
import spring.p2plending.model.Loan;
import spring.p2plending.model.Payment;
import spring.p2plending.model.User;
import spring.p2plending.repository.PaymentRepository;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final LogService logService;
    private final UserService userService;
    private final TransactionService transactionService;
    private final LoanService loanService;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                          LogService logService,
                          UserService userService,
                          TransactionService transactionService,
                          LoanService loanService) {
        this.paymentRepository = paymentRepository;
        this.logService = logService;
        this.userService = userService;
        this.transactionService = transactionService;
        this.loanService = loanService;
    }

    public List<Payment> generatePaymentSchedule(Loan loan) {
        List<Payment> paymentSchedule = new ArrayList<>();

        BigDecimal monthlyInterestRate = BigDecimal.valueOf(loan.getInterestRate() / 100 / 12);
        int totalPayments = loan.getTermInMonths();
        BigDecimal loanAmount = loan.getAmount();

        // Рассчитываем аннуитетный платеж
        BigDecimal monthlyPaymentAmount = calculateAnnuityPayment(loanAmount, monthlyInterestRate, totalPayments);

        LocalDateTime paymentDate = loan.getStartDate().plusMonths(1);

        for (int i = 0; i < totalPayments; i++) {
            BigDecimal interestAmount = loanAmount.multiply(monthlyInterestRate);
            BigDecimal principalAmount = monthlyPaymentAmount.subtract(interestAmount);

            Payment payment = Payment.builder()
                    .loan(loan)
                    .paymentDate(paymentDate)
                    .amount(monthlyPaymentAmount)
                    .interestAmount(interestAmount)
                    .principalAmount(principalAmount)
                    .build();

            paymentSchedule.add(payment);

            loanAmount = loanAmount.subtract(principalAmount);
            paymentDate = paymentDate.plusMonths(1);
        }

        List<Payment> savedPayments = paymentRepository.saveAll(paymentSchedule);

        logService.log("INFO", "PaymentService", "Сгенерирован график платежей для кредита ID=" + loan.getId(), Thread.currentThread().getName());

        return savedPayments;
    }

    private BigDecimal calculateAnnuityPayment(BigDecimal principal, BigDecimal monthlyRate, int totalPayments) {
        BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyRate);
        BigDecimal pow = onePlusRate.pow(totalPayments, MathContext.DECIMAL128);
        BigDecimal numerator = principal.multiply(monthlyRate).multiply(pow);
        BigDecimal denominator = pow.subtract(BigDecimal.ONE);
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }


    public void processPayment(Payment payment, User borrower) {
        if (payment.getIsPaid()) {
            throw new RuntimeException("Платеж уже оплачен.");
        }

        if (borrower.getBalance().compareTo(payment.getAmount()) < 0) {
            throw new RuntimeException("Недостаточно средств на балансе.");
        }

        // Обновляем балансы
        userService.updateUserBalance(borrower, payment.getAmount().negate());
        userService.updateUserBalance(payment.getLoan().getLender(), payment.getAmount());

        payment.setIsPaid(true);
        paymentRepository.save(payment);

        transactionService.createTransaction(
                payment.getLoan(),
                borrower,
                payment.getLoan().getLender(),
                payment.getAmount(),
                TransactionType.PAYMENT_MADE
        );

        logService.log("INFO", "PaymentService", "Платеж ID=" + payment.getId() + " оплачен заемщиком " + borrower.getNickname(), Thread.currentThread().getName());

        loanService.updateLoanStatus(payment.getLoan());
    }
}


