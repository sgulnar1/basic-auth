package aze.coders.basic_authentication.controller;

import aze.coders.basic_authentication.model.SignInRequest;
import aze.coders.basic_authentication.model.SignInResponse;
import aze.coders.basic_authentication.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Value("${spring.security.jwt.expire-time}")
    private Integer expireTime;
    @PostMapping("/sign-in")
    public SignInResponse token (@RequestBody SignInRequest signInRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        String token = jwtService.issueToken(authenticate);
        HttpHeaders headers = new HttpHeaders();
        SignInResponse signInResponse = new SignInResponse(token);
        setCookie(headers, signInResponse);
        return signInResponse;
    }

    public void setCookie(HttpHeaders httpHeaders, SignInResponse signInResponse){
        ResponseCookie responseCookie = ResponseCookie.from("token",signInResponse.getToken())
                .maxAge(expireTime)
                .path("/")
                .httpOnly(true)
                .sameSite("LAX")
                .secure(false).build();
        httpHeaders.set(HttpHeaders.COOKIE, responseCookie.toString());
    }
}
