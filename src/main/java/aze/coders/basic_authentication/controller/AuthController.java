package aze.coders.basic_authentication.controller;

import aze.coders.basic_authentication.config.JwtFilter;
import aze.coders.basic_authentication.model.SignInRequest;
import aze.coders.basic_authentication.model.SignInResponse;
import aze.coders.basic_authentication.service.AuthService;
import aze.coders.basic_authentication.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> token(@RequestBody SignInRequest signInRequest) {
        SignInResponse signInResponse = authService.signIn(signInRequest);
        HttpHeaders headers = new HttpHeaders();
        authService.setCookie(headers, signInResponse);
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(@CookieValue(name = JwtFilter.REFRESH_TOKEN) String refreshToken) {
        authService.signOut(refreshToken);
        HttpHeaders headers = new HttpHeaders();
        authService.clearCookie(headers);
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refresh-token") String refreshToken) {
        SignInResponse signInResponse = authService.refreshToken(refreshToken);
        HttpHeaders headers = new HttpHeaders();
        authService.setCookie(headers, signInResponse);
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);

    }
}
