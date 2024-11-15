package spring.p2plending.сontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import spring.p2plending.dto.LenderOfferRequest;
import spring.p2plending.dto.LenderOfferResponse;
import spring.p2plending.model.LendingOffer;
import spring.p2plending.model.User;
import spring.p2plending.security.CustomUserDetails;
import spring.p2plending.service.LenderOfferService;
import spring.p2plending.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lender/offers")
public class LenderOfferController {

    @Autowired
    private LenderOfferService lenderOfferService;

    @Autowired
    private UserService userService;

    // Эндпоинт для создания предложения (только для кредиторов)
    @PostMapping
    @PreAuthorize("hasRole('ROLE_LENDER')")
    public ResponseEntity<?> createLenderOffer(@RequestBody LenderOfferRequest offerRequest,
                                               Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User lender = userService.findByNickname(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        LendingOffer offer = lenderOfferService.createLenderOffer(lender, offerRequest);
        return ResponseEntity.ok(convertToResponse(offer));
    }

    // Эндпоинт для получения списка активных предложений (доступно всем аутентифицированным пользователям)
    @GetMapping("/active")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getActiveLenderOffers() {
        List<LendingOffer> offers = lenderOfferService.getActiveLenderOffers();
        List<LenderOfferResponse> responses = offers.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // Метод для конвертации LenderOffer в LenderOfferResponse
    private LenderOfferResponse convertToResponse(LendingOffer offer) {

        LenderOfferResponse response = LenderOfferResponse.builder()
                .id(offer.getId())
                .amount(offer.getAmount())
                .interestRate(offer.getInterestRate())
                .termInMonths(offer.getTermInMonths())
                .status(offer.getStatus())
                .createdAt(offer.getCreatedAt())
                .lenderId(offer.getLender().getId())
                .lenderNickname(offer.getLender().getNickname())
                .build();

        return response;
    }
}

