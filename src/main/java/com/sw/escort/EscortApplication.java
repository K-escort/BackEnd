package com.sw.escort;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EscortApplication {

	public static void main(String[] args) {
		SpringApplication.run(EscortApplication.class, args);
	}

}
