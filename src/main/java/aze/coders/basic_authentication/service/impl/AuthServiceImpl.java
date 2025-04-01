package aze.coders.basic_authentication.service.impl;

import aze.coders.basic_authentication.config.JwtFilter;
import aze.coders.basic_authentication.entity.RefreshToken;
import aze.coders.basic_authentication.model.AccessTokenDto;
import aze.coders.basic_authentication.model.RefreshTokenDto;
import aze.coders.basic_authentication.model.SignInRequest;
import aze.coders.basic_authentication.model.SignInResponse;
import aze.coders.basic_authentication.repository.RefreshTokenRepository;
import aze.coders.basic_authentication.service.AuthService;
import aze.coders.basic_authentication.service.JwtService;
import aze.coders.basic_authentication.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${spring.security.jwt.access-expire-time}")
    private Integer accessExpireTime;
    @Value("${spring.security.jwt.refresh-expire-time}")
    private Integer refreshExpireTime;
    private final ObjectMapper objectMapper;

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        String token = jwtService.issueToken(authenticate);
        AccessTokenDto accessTokenDto = new AccessTokenDto(token);
        RefreshTokenDto refreshTokenDto = issueRefreshToken(signInRequest.getUsername());
        return new SignInResponse(accessTokenDto, refreshTokenDto);
    }

    private RefreshTokenDto issueRefreshToken(String username) {
        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), username, true, new Date(), new Date(System.currentTimeMillis() + refreshExpireTime));
        refreshTokenRepository.save(refreshToken);
        return objectMapper.convertValue(refreshToken, RefreshTokenDto.class);
    }

    @Override
    public void clearCookie(HttpHeaders headers) {
        ResponseCookie accessCookie = ResponseCookie.from(JwtFilter.ACCESS_TOKEN, "")
                .maxAge(0)
                .path("/")
                .httpOnly(true)
                .sameSite("LAX")
                .secure(false).build();
        headers.add(HttpHeaders.SET_COOKIE, accessCookie.toString());
        ResponseCookie refreshCookie = ResponseCookie.from(JwtFilter.REFRESH_TOKEN, "")
                .maxAge(0)
                .path("/")
                .httpOnly(true)
                .sameSite("LAX")
                .secure(false).build();
        headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

    @Override
    public void setCookie(HttpHeaders httpHeaders, SignInResponse signInResponse) {
        ResponseCookie accessCookie = ResponseCookie.from(JwtFilter.ACCESS_TOKEN, signInResponse.getAccessToken().getToken())
                .maxAge(accessExpireTime)
                .path("/")
                .httpOnly(true)
                .sameSite("LAX")
                .secure(false).build();
        httpHeaders.add(HttpHeaders.SET_COOKIE, accessCookie.toString());
        ResponseCookie refreshCookie = ResponseCookie.from(JwtFilter.REFRESH_TOKEN, signInResponse.getRefreshToken().getToken())
                .maxAge(accessExpireTime)
                .path("/")
                .httpOnly(true)
                .sameSite("LAX")
                .secure(false).build();
        httpHeaders.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

    @Override
    public void signOut(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken).
                ifPresent(refresh -> {
                    refresh.setValid(false);
                    refreshTokenRepository.save(refresh);
                });
    }

    @Override
    public SignInResponse refreshToken(String refreshToken) {
        RefreshToken refreshTokenDb = refreshTokenRepository.findByToken(refreshToken).orElseThrow(
                () -> new BadCredentialsException("Refresh not found"));
        if (!refreshTokenDb.getValid())
            throw new BadCredentialsException("Invalid refresh token");
        if (refreshTokenDb.getExpiryDate().before(new Date()))
            throw new BadCredentialsException("Expired refresh token");
        UserDetails userDetails = userService.loadUserByUsername(refreshTokenDb.getUsername());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword());
        AccessTokenDto accessTokenDto = new AccessTokenDto(jwtService.issueToken(usernamePasswordAuthenticationToken));
        refreshTokenDb.setValid(false);
        refreshTokenRepository.save(refreshTokenDb);
        RefreshTokenDto refreshTokenDto = issueRefreshToken(refreshTokenDb.getUsername());
        return new SignInResponse(accessTokenDto, refreshTokenDto);
    }
}
