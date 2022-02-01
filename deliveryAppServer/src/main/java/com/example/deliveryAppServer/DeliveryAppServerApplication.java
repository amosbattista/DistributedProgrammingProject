package com.example.deliveryAppServer;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@EnableJpaRepositories
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })

public class DeliveryAppServerApplication {


	public static void main(String[] args) {
		SpringApplication.run(DeliveryAppServerApplication.class, args);
	}

}


