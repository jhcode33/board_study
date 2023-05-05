package com.board.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // JPA Auditing
@SpringBootApplication
public class BoardStudyApplication {
	public static void main(String[] args) {
		SpringApplication.run(BoardStudyApplication.class, args);
		
		System.out.println("병합테스트");
	}

}
