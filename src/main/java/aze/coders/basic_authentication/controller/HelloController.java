package aze.coders.basic_authentication.controller;

import aze.coders.basic_authentication.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    HelloService helloService;

    @GetMapping("/public")
    public String helloPublic() {
        return "Hello Public";
    }
    @GetMapping("/auth")
    public String helloAuth() {
        return "Hello Auth";
    }
    @GetMapping("/user")
    public String helloUser() {
        return "Hello User";
    }
    @GetMapping("/admin")
    public String helloAdmin() {
        return "Hello Admin";
    }
    @GetMapping("/method")
    public String helloMethod() {
        return helloService.sayHelloMethodAdmin();
    }
}
