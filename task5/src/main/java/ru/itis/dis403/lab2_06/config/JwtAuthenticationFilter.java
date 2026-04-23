package ru.itis.dis403.lab2_06.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itis.dis403.lab2_06.service.JWTService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 1. Проверяем, есть ли заголовок и начинается ли он с "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Если токена нет, просто пропускаем фильтр дальше (цепочку)
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 2. Извлекаем токен (убираем "Bearer ")
            jwt = authHeader.substring(7);

            // 3. Извлекаем имя пользователя из токена
            username = jwtService.extractUsername(jwt);

            // 4. Если имя есть и в контексте безопасности еще никого нет
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 5. Валидируем токен
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            // Продолжаем цепочку фильтров
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // ЛОВИМ ВСЕ ОШИБКИ JWT (неверный формат, истек срок, слабый ключ и т.д.)
            // И просто пропускаем запрос дальше, не ломая приложение
            filterChain.doFilter(request, response);
        }

    }
}
