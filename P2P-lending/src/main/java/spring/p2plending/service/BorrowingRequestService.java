package spring.p2plending.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.p2plending.enums.RequestStatus;
import spring.p2plending.model.BorrowingRequest;
import spring.p2plending.model.User;
import spring.p2plending.repository.BorrowingRequestRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowingRequestService {

    private final BorrowingRequestRepository borrowingRequestRepository;
    private final LogService logService;

    @Autowired
    public BorrowingRequestService(BorrowingRequestRepository borrowingRequestRepository, LogService logService) {
        this.borrowingRequestRepository = borrowingRequestRepository;
        this.logService = logService;
    }

    public BorrowingRequest createBorrowingRequest(User borrower, BorrowingRequest request) {
        request.setBorrower(borrower);
        request.setCreatedAt(LocalDateTime.now());
        request.setStatus(RequestStatus.OPEN);

        BorrowingRequest savedRequest = borrowingRequestRepository.save(request);

        logService.log("INFO", "BorrowingRequestService", "Создан новый запрос на кредит: ID=" + savedRequest.getId() + " от Заемщика=" + borrower.getNickname(), Thread.currentThread().getName());

        return savedRequest;
    }

    public List<BorrowingRequest> getOpenBorrowingRequests() {
        return borrowingRequestRepository.findByStatus(RequestStatus.OPEN);
    }

}
