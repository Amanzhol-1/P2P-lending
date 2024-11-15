package spring.p2plending.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.p2plending.dto.RegisterRequest;
import spring.p2plending.enums.Role;
import spring.p2plending.model.User;
import spring.p2plending.repository.UserRepository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LogService logService; // Инъекция LogService

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, LogService logService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.logService = logService;
    }

    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByNickname(registerRequest.getNickname())) {
            logService.log("WARN", "UserService", "Попытка регистрации с уже существующим никнеймом: " + registerRequest.getNickname(), Thread.currentThread().getName(), "NicknameAlreadyTaken");
            throw new RuntimeException("Nickname is already taken!");
        }

        User user = User.builder()
                .nickname(registerRequest.getNickname())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .build();

        Set<Role> roles = new HashSet<>();
        if (registerRequest.getRoles() == null || registerRequest.getRoles().isEmpty()) {
            roles.add(Role.ROLE_BORROWER);
        } else {
            for (String roleStr : registerRequest.getRoles()) {
                try {
                    Role role = Role.valueOf(roleStr.toUpperCase());
                    roles.add(role);
                } catch (IllegalArgumentException e) {
                    logService.log("ERROR", "UserService", "Неизвестная роль: " + roleStr, Thread.currentThread().getName(), e.toString());
                    throw new RuntimeException("Role " + roleStr + " does not exist.");
                }
            }
        }

        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        logService.log("INFO", "UserService", "Новый пользователь зарегистрирован: " + savedUser.getNickname(), Thread.currentThread().getName());

        return savedUser;
    }

    @Transactional
    public void updateUserBalance(User user, BigDecimal amount) {
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);

        logService.log("INFO", "UserService", "Баланс пользователя " + user.getNickname() + " обновлен на " + amount, Thread.currentThread().getName());
    }

    public Optional<User> findByNickname(String nickname) {
        Optional<User> userOpt = userRepository.findByNickname(nickname);
        if (userOpt.isPresent()) {
            logService.log("INFO", "UserService", "Пользователь найден: " + nickname, Thread.currentThread().getName());
        } else {
            logService.log("WARN", "UserService", "Пользователь не найден: " + nickname, Thread.currentThread().getName());
        }
        return userOpt;
    }
}

