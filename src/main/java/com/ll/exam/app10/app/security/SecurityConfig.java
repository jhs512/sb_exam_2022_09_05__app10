package com.ll.exam.app10.app.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final OAuth2UserService oAuth2UserService;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(
                        csrf -> csrf.disable()
                )
                .authorizeRequests(
                        authorizeRequests -> authorizeRequests
                                .antMatchers("/**")
                                .permitAll()
                )
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/member/login") // GET
                                .loginProcessingUrl("/member/login") // POST
                                .successHandler(authenticationSuccessHandler)
                )
                .oauth2Login(
                        oauth2Login -> oauth2Login
                                .loginPage("/member/login")
                                .successHandler(authenticationSuccessHandler)
                                .userInfoEndpoint(
                                        userInfoEndpoint -> userInfoEndpoint
                                                .userService(oAuth2UserService)
                                )
                )
                .logout(logout -> logout
                        .logoutUrl("/member/logout")
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
