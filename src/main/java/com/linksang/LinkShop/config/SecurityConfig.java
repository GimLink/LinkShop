package com.linksang.LinkShop.config;

import com.linksang.LinkShop.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomLoginFailureHandler failureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/member/findId", "/member/idConfirm", "/member/sendAuth", "/member/authNumCheck",
                        "/member/findId/sendMessage", "/member/findId/authNum", "/member/findId/findIdResult", "image/**").permitAll()
                .requestMatchers("/kakao/login", "/join", "/login").permitAll()
                .requestMatchers("/mypage/**", "/member/**", "/kakao/**", "/cart/**", "/order/**", "/board/write/**", "/success").authenticated()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/**").permitAll());



        http.csrf(CsrfConfigurer::disable);

        http.exceptionHandling(ex -> {
            ex.accessDeniedPage("/");
        });

        http.formLogin(form -> {
            form
                    .loginPage("/login")
                    .loginProcessingUrl("/loginProc")
                    .defaultSuccessUrl("/")
                    .usernameParameter("userId")
                    .successHandler(successHandler())
                    .failureHandler(failureHandler);
        });


        http.logout(logout -> {
            logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .invalidateHttpSession(true)
                    .logoutSuccessUrl("/");
        });

        return http.build();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
//        return auth.build();
//    }

//    private AuthenticationFilter getAuthenticationFilter() throws Exception {
//        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
//        AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(objectPostProcessor);
//        authenticationFilter.setAuthenticationManager(authenticationManager(builder));
//        return authenticationFilter;
//    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {return new CustomLoginSuccessHandler("/");}

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
