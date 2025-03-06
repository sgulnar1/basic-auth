package aze.coders.basic_authentication.controller;

import aze.coders.basic_authentication.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jwt")
public class JwtController {
    @Autowired
    JwtService jwtService;

    @GetMapping("/issue-token")
    public String jwt(Authentication authentication) {
        System.out.println("authentication: " + authentication);
        System.out.println("authentication: " + authentication.isAuthenticated());
        System.out.println("authentication: " + authentication.getName());
        System.out.println("authentication: " + authentication.getDetails());
        System.out.println("authentication: " + authentication.getCredentials());
        System.out.println("authentication: " + authentication.getPrincipal());
        System.out.println("authentication: " + authentication.getAuthorities());
        return jwtService.issueToken(authentication);
    }
    @GetMapping("/parse-token")
    public Object parseToken(@RequestParam String token) {
        return jwtService.parseToken(token);
    }

}
