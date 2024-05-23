package com.linksang.LinkShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LinkShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinkShopApplication.class, args);
	}

}
