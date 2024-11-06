package spring.p2plending.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.p2plending.enums.LoanStatus;
import spring.p2plending.model.Loan;
import spring.p2plending.model.User;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByBorrower(User borrower);
    List<Loan> findByLender(User lender);
    List<Loan> findByStatus(LoanStatus status);
}
