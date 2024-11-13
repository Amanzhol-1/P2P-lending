package spring.p2plending.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.p2plending.model.LendingOffer;
import spring.p2plending.enums.OfferStatus;

import java.util.List;

@Repository
public interface LenderOfferRepository extends JpaRepository<LendingOffer, Long> {
    List<LendingOffer> findByStatus(OfferStatus status);
}
