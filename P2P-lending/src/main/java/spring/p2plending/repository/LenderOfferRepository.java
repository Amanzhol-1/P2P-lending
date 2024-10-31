package spring.p2plending.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.p2plending.model.LendingOffer;

import java.util.List;

public interface LenderOfferRepository extends JpaRepository<LendingOffer, Long> {
    List<LendingOffer> findByStatus(spring.p2plending.enums.OfferStatus status);
}

