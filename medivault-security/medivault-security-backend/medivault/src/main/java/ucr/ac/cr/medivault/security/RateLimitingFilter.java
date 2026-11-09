package ucr.ac.cr.medivault.security;

import java.io.IOException;
import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Bucket authBucket = Bucket.builder()
            .addLimit(Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1))))
            .build();

    private final Bucket apiBucket = Bucket.builder()
            .addLimit(Bandwidth.classic(30, Refill.intervally(30, Duration.ofMinutes(1))))
            .build();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        Bucket target = null;

        if (path.startsWith("/api/v1/auth/login")) {
            target = authBucket;
        } else if (path.startsWith("/api/v1/expedientes")) {
            target = apiBucket;
        }

        if (target == null || target.tryConsume(1)) {
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Retry-After", "60");

        String json = """
                {
                  "type": "%s",
                  "title": "%s",
                  "status": 429,
                  "detail": "%s",
                  "instance": "%s"
                }
                """.formatted(
                "https://medivault.cr/rl",
                "Demasiadas Peticiones",
                "Limite superado. Espere 60s.",
                path);
        response.getWriter().write(json);
    }
}