package aze.coders.basic_authentication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponse {
    private AccessTokenDto accessToken;
    private RefreshTokenDto refreshToken;
}
