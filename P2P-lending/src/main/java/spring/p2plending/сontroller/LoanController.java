package spring.p2plending.сontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import spring.p2plending.model.Loan;
import spring.p2plending.model.User;
import spring.p2plending.security.CustomUserDetails;
import spring.p2plending.service.LenderOfferService;
import spring.p2plending.service.UserService;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LenderOfferService lenderOfferService;
    private final UserService userService;

    @Autowired
    public LoanController(LenderOfferService lenderOfferService, UserService userService) {
        this.lenderOfferService = lenderOfferService;
        this.userService = userService;
    }

    // Эндпоинт для заемщика, чтобы принять предложение кредитора
    @PostMapping("/accept-offer/{offerId}")
    @PreAuthorize("hasRole('ROLE_BORROWER')")
    public ResponseEntity<?> acceptLenderOffer(@PathVariable Long offerId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User borrower = userService.findByNickname(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        try {
            Loan loan = lenderOfferService.acceptLenderOffer(borrower, offerId);
            return ResponseEntity.ok("Кредит успешно создан с ID: " + loan.getId());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
