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

    @Autowired
    private LenderOfferRepository lenderOfferRepository;

    @Autowired
    private UserRepository userRepository;

    public LendingOffer createLenderOffer(User lender, LenderOfferRequest offerRequest) {
        LendingOffer offer = new LendingOffer();
        offer.setAmount(offerRequest.getAmount());
        offer.setInterestRate(offerRequest.getInterestRate());
        offer.setTermInMonths(offerRequest.getTermInMonths());
        offer.setStatus(OfferStatus.ACTIVE);
        offer.setCreatedAt(LocalDateTime.now());
        offer.setLender(lender);

        return lenderOfferRepository.save(offer);
    }

    public List<LendingOffer> getActiveLenderOffers() {
        return lenderOfferRepository.findByStatus(OfferStatus.ACTIVE);
    }
}
