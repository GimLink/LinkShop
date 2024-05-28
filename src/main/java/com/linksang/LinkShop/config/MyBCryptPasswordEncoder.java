package com.linksang.LinkShop.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyBCryptPasswordEncoder extends BCryptPasswordEncoder {
}
