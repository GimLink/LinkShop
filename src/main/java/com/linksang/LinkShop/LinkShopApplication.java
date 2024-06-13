package com.linksang.LinkShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LinkShopApplication {

	private static final String YMLS = "spring.config.location=" +
			"classpath:/application.yml," +
			"classpath:/aws.yml," +
			"classpath:/redis.yml," +
			"classpath:/toss.yml," +
			"classpath:/coolsms.yml"
			;

	public static void main(String[] args) {

		new SpringApplicationBuilder(LinkShopApplication.class)
				.properties(YMLS)
						.run(args);
	}

}
