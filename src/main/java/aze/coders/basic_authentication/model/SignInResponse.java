package aze.coders.basic_authentication.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInResponse {
    private String token;
}
