package spring.p2plending.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import spring.p2plending.enums.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Data
@ToString
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lender_id", nullable = false)
    private User lender;

    @ManyToOne
    @JoinColumn(name = "borrower_id", nullable = false)
    private User borrower;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "interest_rate", nullable = false)
    private Double interestRate;

    @Column(name = "term_in_months", nullable = false)
    private Integer termInMonths;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate = LocalDateTime.now();

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanStatus status = LoanStatus.ACTIVE;

}

