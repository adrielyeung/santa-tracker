package com.adriel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan("com.adriel")
public class SantaTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SantaTrackerApplication.class, args);
	}

}
