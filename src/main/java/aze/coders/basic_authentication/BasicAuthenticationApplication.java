package aze.coders.basic_authentication;

import aze.coders.basic_authentication.entity.Authority;
import aze.coders.basic_authentication.entity.User;
import aze.coders.basic_authentication.repository.AuthorityRepository;
import aze.coders.basic_authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class BasicAuthenticationApplication implements CommandLineRunner {
	private final AuthorityRepository authorityRepository;
	private final PasswordEncoder passwordEncoder;
	@Autowired
	UserRepository userRepository;

	@Override
	public void run(String... args) throws Exception {
//		Authority authorityUser = new Authority();
//		authorityUser.setAuthority("ROLE_USER");
//		authorityRepository.save(authorityUser);
//		Authority authorityAdmin = new Authority();
//		authorityAdmin.setAuthority("ROLE_ADMIN");
//		authorityRepository.save(authorityAdmin);
//		User user = new User();
//		user.setUsername("user");
//		user.setPassword(passwordEncoder.encode("1234"));
//		user.setAuthorities(List.of(authorityUser));
//		userRepository.save(user);
//		User userAdmin = new User();
//		userAdmin.setUsername("admin");
//		userAdmin.setPassword(passwordEncoder.encode("12345"));
//		userAdmin.setAuthorities(List.of(authorityUser, authorityAdmin));
//		userRepository.save(userAdmin);
	}

	public static void main(String[] args) {
		SpringApplication.run(BasicAuthenticationApplication.class, args);
	}

}
