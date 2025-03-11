package aze.coders.basic_authentication.model;

import lombok.Data;

@Data
public class SignInRequest {
    private String username;
    private String password;
}
