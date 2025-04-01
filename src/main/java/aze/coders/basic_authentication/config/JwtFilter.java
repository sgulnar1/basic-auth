package aze.coders.basic_authentication.config;

import aze.coders.basic_authentication.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.net.Authenticator;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    public static final String ACCESS_TOKEN = "access-token";
    public static final String REFRESH_TOKEN = "refresh-token";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        Optional<String> token = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION));
//        if(token.isPresent())
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Arrays.stream(cookies).filter(
                            cookie -> cookie.getName().equals(ACCESS_TOKEN)).findFirst().
                    ifPresent(cookie ->
                    {
                        Claims claims = jwtService.parseToken(cookie.getValue());
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(claims.get("principal"), null, getAuthorities(claims));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    });
        }
        filterChain.doFilter(request, response);
    }

    private List<? extends GrantedAuthority> getAuthorities(Claims claims) {
        System.out.println("authorities: " + claims.get("authorities"));
        List<String> roles = claims.get("authorities", List.class);
        return roles.stream().map(SimpleGrantedAuthority::new).toList();
    }
}
