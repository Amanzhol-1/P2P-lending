package spring.p2plending.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import spring.p2plending.enums.OfferStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "lender_offers")
@Data
@ToString
public class LendingOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lender_id", nullable = false)
    private User lender;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "interest_rate", nullable = false)
    private Double interestRate;

    @Column(name = "term_in_months", nullable = false)
    private Integer termInMonths;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OfferStatus status = OfferStatus.ACTIVE;
}
