package spring.p2plending.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.p2plending.dto.LenderOfferRequest;
import spring.p2plending.enums.OfferStatus;
import spring.p2plending.model.LendingOffer;
import spring.p2plending.model.User;
import spring.p2plending.repository.LenderOfferRepository;
import spring.p2plending.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LenderOfferService {

    private final LenderOfferRepository lenderOfferRepository;
    private final UserRepository userRepository;
    private final LogService logService; // Инъекция LogService

    @Autowired
    public LenderOfferService(LenderOfferRepository lenderOfferRepository, UserRepository userRepository, LogService logService) {
        this.lenderOfferRepository = lenderOfferRepository;
        this.userRepository = userRepository;
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
}
