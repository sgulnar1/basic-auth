package aze.coders.basic_authentication.service;

import org.springframework.security.core.Authentication;

public interface JwtService {
    String issueToken(Authentication authentication);

    Object parseToken(String token);
}
