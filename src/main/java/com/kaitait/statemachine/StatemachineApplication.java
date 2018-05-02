package com.kaitait.statemachine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.kaitait")
@SpringBootApplication
public class StatemachineApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatemachineApplication.class, args);
	}
}
