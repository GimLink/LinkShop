package com.linksang.LinkShop.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {


//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//
//        setDefaultFailureUrl("/login");
//
//        if (exception instanceof BadCredentialsException) {
//            request.setAttribute("LoginFailureMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
//        }
//
//        request.setAttribute("userId", request.getParameter("userId"));
//        request.setAttribute("password", request.getParameter("password"));
//
//        request.getRequestDispatcher("/login").forward(request, response);
//    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        setUseForward(true);

        if (exception instanceof BadCredentialsException) {

            request.setAttribute("LoginFailureMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        setDefaultFailureUrl("/login?error=true");

        request.setAttribute("userId", request.getParameter("userId"));
        request.setAttribute("password", request.getParameter("password"));


        super.onAuthenticationFailure(request, response, exception);
    }
}
