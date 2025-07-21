package com.example.likelion13spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //16주차 추가
public class Likelion13springApplication {

	public static void main(String[] args) {

		SpringApplication.run(Likelion13springApplication.class, args);
	}

}
