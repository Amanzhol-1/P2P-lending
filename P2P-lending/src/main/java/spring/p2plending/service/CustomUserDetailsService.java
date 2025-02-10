package spring.p2plending.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring.p2plending.model.User;
import spring.p2plending.repository.UserRepository;
import spring.p2plending.security.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final LogService logService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

            logService.log("INFO", "CustomUserDetailsService", "Пользователь аутентифицирован: " + username, Thread.currentThread().getName());

            return new CustomUserDetails(user);
        } catch (UsernameNotFoundException e) {
            logService.log("WARN", "CustomUserDetailsService", "Не удалось найти пользователя: " + username, Thread.currentThread().getName(), e.toString());
            throw e;
        }
    }
}
