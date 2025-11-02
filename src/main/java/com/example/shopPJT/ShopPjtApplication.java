package com.example.shopPJT;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShopPjtApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopPjtApplication.class, args);
	}

}
