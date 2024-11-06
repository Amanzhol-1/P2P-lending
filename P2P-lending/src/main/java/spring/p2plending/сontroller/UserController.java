package spring.p2plending.сontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import spring.p2plending.dto.BalanceRequest;
import spring.p2plending.model.User;
import spring.p2plending.security.CustomUserDetails;
import spring.p2plending.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/balance/top-up")
    public ResponseEntity<?> topUpBalance(@RequestBody BalanceRequest balanceRequest,
                                          Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userService.findByNickname(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        userService.updateUserBalance(user, balanceRequest.getAmount());

        return ResponseEntity.ok("Баланс успешно пополнен.");
    }

    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userService.findByNickname(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok("Ваш текущий баланс: " + user.getBalance());
    }
}
