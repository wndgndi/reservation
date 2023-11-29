package com.example.reservation.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception {
        http
            .httpBasic().disable()
            .csrf().disable()
            // 세션을 사용하지 않으므로 Stateless로 설정
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()

            // exception handling 시에 사용할 클래스 추가
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)

            .and()
            .authorizeRequests()
            .antMatchers("/**/signup", "/**/signin", "/store/search").permitAll()

            .antMatchers(HttpMethod.POST, "/review/**").hasRole("USER")
            .antMatchers(HttpMethod.PUT, "/review/**").hasRole("USER")
            .antMatchers(HttpMethod.DELETE, "/review").hasAnyRole("USER", "PARTNER")
            .antMatchers(HttpMethod.POST, "/reservation/**").hasAnyRole("USER", "PARTNER")
            .antMatchers(HttpMethod.PUT, "/reservation/**").hasAnyRole("USER", "PARTNER")
            .antMatchers(HttpMethod.PUT, "/reservation/**").hasAnyRole("USER", "PARTNER")
            .antMatchers(HttpMethod.POST, "/store/**").hasRole("PARTNER")
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
