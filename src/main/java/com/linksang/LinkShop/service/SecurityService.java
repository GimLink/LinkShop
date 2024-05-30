package com.linksang.LinkShop.service;

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public boolean isAuthenticated() {
        AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
        return (!trustResolver.isAnonymous(SecurityContextHolder.getContext().getAuthentication()));
    }

    public String getName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public boolean compareName(String userName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName().equals(userName);
    }

    public boolean checkHasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().contains(new SimpleGrantedAuthority(role));
    }
}
