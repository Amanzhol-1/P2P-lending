package spring.p2plending.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.p2plending.dto.RegisterRequestDTO;
import spring.p2plending.enums.Role;
import spring.p2plending.exception.UsernameAlreadyTakenException;
import spring.p2plending.model.User;
import spring.p2plending.repository.UserRepository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(RegisterRequestDTO req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new UsernameAlreadyTakenException("Username is already taken!");
        }
        Set<Role> roles = new HashSet<>();
        if (req.getRoles() == null || req.getRoles().isEmpty()) {
            roles.add(Role.ROLE_USER);
        } else {
            req.getRoles().forEach(r -> roles.add(Role.valueOf(r.toUpperCase())));
        }
        User user = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .balance(BigDecimal.ZERO)
                .roles(roles)
                .build();
        return userRepository.save(user);
    }
}


