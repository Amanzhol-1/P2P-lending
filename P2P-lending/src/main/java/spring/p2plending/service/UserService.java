package spring.p2plending.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.p2plending.dto.RegisterRequest;
import spring.p2plending.enums.Role;
import spring.p2plending.model.User;
import spring.p2plending.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByNickname(registerRequest.getNickname())) {
            throw new RuntimeException("Nickname is already taken!");
        }

        User user = new User();
        user.setNickname(registerRequest.getNickname());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());

        Set<Role> roles = new HashSet<>();
        if (registerRequest.getRoles() == null || registerRequest.getRoles().isEmpty()) {
            roles.add(Role.ROLE_BORROWER);
        } else {
            for (String roleStr : registerRequest.getRoles()) {
                try {
                    Role role = Role.valueOf(roleStr.toUpperCase());
                    roles.add(role);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Role " + roleStr + " does not exist.");
                }
            }
        }

        user.setRoles(roles);

        return userRepository.save(user);
    }

    public Optional<User> findByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }
}
