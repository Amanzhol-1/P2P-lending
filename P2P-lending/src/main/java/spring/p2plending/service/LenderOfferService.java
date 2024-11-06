package spring.p2plending.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.p2plending.dto.LenderOfferRequest;
import spring.p2plending.enums.LoanStatus;
import spring.p2plending.enums.OfferStatus;
import spring.p2plending.enums.TransactionType;
import spring.p2plending.model.*;
import spring.p2plending.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LenderOfferService {

    private final LenderOfferRepository lenderOfferRepository;
    private final LoanRepository loanRepository;
    private final PaymentService paymentService;
    private final TransactionService transactionService;
    private final UserService userService;
    private final LogService logService;

    @Autowired
    public LenderOfferService(LenderOfferRepository lenderOfferRepository,
                              LoanRepository loanRepository,
                              PaymentService paymentService,
                              TransactionService transactionService,
                              UserService userService,
                              LogService logService) {
        this.lenderOfferRepository = lenderOfferRepository;
        this.loanRepository = loanRepository;
        this.paymentService = paymentService;
        this.transactionService = transactionService;
        this.userService = userService;
        this.logService = logService;
    }

    public LendingOffer createLenderOffer(User lender, LenderOfferRequest offerRequest) {
        LendingOffer offer = new LendingOffer();
        offer.setAmount(offerRequest.getAmount());
        offer.setInterestRate(offerRequest.getInterestRate());
        offer.setTermInMonths(offerRequest.getTermInMonths());
        offer.setStatus(OfferStatus.ACTIVE);
        offer.setCreatedAt(LocalDateTime.now());
        offer.setLender(lender);

        LendingOffer savedOffer = lenderOfferRepository.save(offer);

        logService.log("INFO", "LenderOfferService", "Новое предложение создано: ID=" + savedOffer.getId() + " от Лендера=" + lender.getNickname(), Thread.currentThread().getName());

        return savedOffer;
    }

    public List<LendingOffer> getActiveLenderOffers() {
        List<LendingOffer> activeOffers = lenderOfferRepository.findByStatus(OfferStatus.ACTIVE);
        logService.log("INFO", "LenderOfferService", "Получено " + activeOffers.size() + " активных предложений от лендера.", Thread.currentThread().getName());
        return activeOffers;
    }

    @Transactional
    public Loan acceptLenderOffer(User borrower, Long offerId) {
        LendingOffer offer = lenderOfferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Предложение кредитора не найдено"));

        if (offer.getStatus() != OfferStatus.ACTIVE) {
            throw new RuntimeException("Предложение кредитора не активно");
        }

        if (offer.getLender().getBalance().compareTo(offer.getAmount()) < 0) {
            throw new RuntimeException("У кредитора недостаточно средств для выдачи кредита");
        }

        offer.setStatus(OfferStatus.ACCEPTED);
        lenderOfferRepository.save(offer);

        Loan loan = new Loan();
        loan.setBorrower(borrower);
        loan.setLender(offer.getLender());
        loan.setAmount(offer.getAmount());
        loan.setInterestRate(offer.getInterestRate());
        loan.setTermInMonths(offer.getTermInMonths());
        loan.setStartDate(LocalDateTime.now());
        loan.setEndDate(LocalDateTime.now().plusMonths(offer.getTermInMonths()));
        loan.setStatus(LoanStatus.ACTIVE);

        Loan savedLoan = loanRepository.save(loan);

        paymentService.generatePaymentSchedule(savedLoan);

        transactionService.createTransaction(
                savedLoan,
                offer.getLender(),
                borrower,
                offer.getAmount(),
                TransactionType.LOAN_ISSUED
        );

        userService.updateUserBalance(offer.getLender(), offer.getAmount().negate());
        userService.updateUserBalance(borrower, offer.getAmount());

        logService.log("INFO", "LenderOfferService", "Заемщик " + borrower.getNickname() + " принял предложение ID=" + offer.getId() + ", создан кредит ID=" + savedLoan.getId(), Thread.currentThread().getName());

        return savedLoan;
    }
}
