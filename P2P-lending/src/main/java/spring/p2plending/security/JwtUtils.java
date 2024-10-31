package spring.p2plending.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${spring.p2plending.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.p2plending.jwtExpirationMs}")
    private int jwtExpirationMs;

    // Метод для получения ключа подписи
    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Генерация jwt токена
    public String generateJwtToken(CustomUserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // Используем nickname как subject
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512) // Указываем ключ и алгоритм
                .compact();
    }

    // Получение nickname из JWT токена
    public String getNicknameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey()) // Устанавливаем ключ подписи
                .build()
                .parseClaimsJws(token) // Парсим токен
                .getBody()
                .getSubject(); // Получаем subject
    }

    // Валидация JWT токена
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(getSigningKey()) // Устанавливаем ключ подписи
                    .build()
                    .parseClaimsJws(authToken); // Парсим токен
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("Invalid JWT token: " + e.getMessage());
        }
        return false;
    }
}


