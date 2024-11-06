package spring.p2plending.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.p2plending.enums.RequestStatus;
import spring.p2plending.model.BorrowingRequest;

import java.util.List;

@Repository
public interface BorrowingRequestRepository extends JpaRepository<BorrowingRequest, Long> {
    List<BorrowingRequest> findByStatus(RequestStatus status);
}
