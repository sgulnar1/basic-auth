package aze.coders.basic_authentication.service;

import aze.coders.basic_authentication.model.SignInRequest;
import aze.coders.basic_authentication.model.SignInResponse;
import org.springframework.http.HttpHeaders;

public interface AuthService {
    SignInResponse signIn(SignInRequest signInRequest);

    void clearCookie(HttpHeaders headers);

    void setCookie(HttpHeaders headers, SignInResponse signInResponse);

    void signOut(String refreshToken);

    SignInResponse refreshToken(String refreshToken);
}
