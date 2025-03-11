package aze.coders.basic_authentication.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringBasicAuthConfig {
    @Autowired
    JwtFilter jwtFilter;
    //private final JwtFilterConfigurerAdapter jwtFilterConfigurerAdapter;

    //    @Bean
//    InMemoryUserDetailsManager userDetailsManager() {
//        List<UserDetails> users = new ArrayList<>();
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//        users.add(new User("user", "123", authorities));
//        return new InMemoryUserDetailsManager(
//                new User("guest", "{noop}123", Collections.EMPTY_LIST),
//                new User("user", "{noop}1234", authorities),
//                new User("admin", "{noop}12345", List.of(new SimpleGrantedAuthority("ROLE_USER"),
//                        new SimpleGrantedAuthority("ROLE_ADMIN"))));
//        //return new InMemoryUserDetailsManager(users);
//    }
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/public/**", "/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                                .requestMatchers("/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                ).formLogin(AbstractHttpConfigurer::disable)
                //formLogin(Customizer.withDefaults())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        //http.apply(jwtFilterConfigurerAdapter);

        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
