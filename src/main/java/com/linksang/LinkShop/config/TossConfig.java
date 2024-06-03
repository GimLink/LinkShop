package com.linksang.LinkShop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TossConfig {

    @Value("${payment.toss.test_client_api_key}")
    private String CLIENT_KEY;

    @Value("${payment.toss.test_secret_api_key")
    private String SECRET_KEY;
}
