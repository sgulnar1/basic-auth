//package aze.coders.basic_authentication.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//public class JwtFilterConfigurerAdapter extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
//    @Autowired
//    JwtFilter jwtFilter;
//
//    @Override
//    public void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//    }
//
//}
