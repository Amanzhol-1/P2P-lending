package spring.p2plending.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import spring.p2plending.model.User;
import spring.p2plending.repository.UserRepository;
import spring.p2plending.security.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final LogService logService;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, LogService logService) {
        this.userRepository = userRepository;
        this.logService = logService;
    }

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByNickname(nickname)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with nickname: " + nickname));

            logService.log("INFO", "CustomUserDetailsService", "Пользователь аутентифицирован: " + nickname, Thread.currentThread().getName());

            return new CustomUserDetails(user);
        } catch (UsernameNotFoundException e) {
            logService.log("WARN", "CustomUserDetailsService", "Не удалось найти пользователя: " + nickname, Thread.currentThread().getName(), e.toString());
            throw e;
        }
    }
}
