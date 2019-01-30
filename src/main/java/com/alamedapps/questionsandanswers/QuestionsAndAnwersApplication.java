package com.alamedapps.questionsandanswers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableJpaAuditing
public class QuestionsAndAnwersApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuestionsAndAnwersApplication.class, args);
	}

//	CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//		return args -> {
//			initUsers(userRepository, passwordEncoder);
//		};
//	}
//
//	private void initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//		User admin = new User();
//		admin.setNome("admin");
//		admin.setEmail("admin@gmail.com");
//		admin.setPassword(passwordEncoder.encode("123456"));
//
//		User find = userRepository.findByEmail("admin@gmail.com");
//		if (find == null)
//			userRepository.save(admin);
//
//	}

}
