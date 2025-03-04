package aze.coders.basic_authentication.service.impl;

import aze.coders.basic_authentication.service.HelloService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl implements HelloService {
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public String sayHelloMethodAdmin() {
        return "Hello admin with method";
    }
}
