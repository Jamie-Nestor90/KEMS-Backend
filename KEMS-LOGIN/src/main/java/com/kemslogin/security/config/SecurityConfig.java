package com.kemslogin.security.config;

import com.kemslogin.security.jwt.CustomUserDetailsService;
import com.kemslogin.security.jwt.JwtAuthenticationFilter;
import com.kemslogin.security.jwt.JwtUtil;
import com.kemslogin.security.jwt.LoginFilter;
import com.kemslogin.security.oauth2.CustomOAuth2FailureHandler;
import com.kemslogin.security.oauth2.CustomOAuth2SuccessHandler;
import com.kemslogin.security.oauth2.CustomOAuth2UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final CustomOAuth2FailureHandler customOAuth2FailureHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://172.30.1.84:3000", "http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityfilterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception {
        http.csrf((csrf) -> csrf.disable());
        http.formLogin((form) -> form.disable());
        http.httpBasic((basic) -> basic.disable());
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/oauth2/**").permitAll()
                        .anyRequest().authenticated());

        //LoginFilter 추가
        http.addFilterBefore(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, "/api/auth/user/login"), UsernamePasswordAuthenticationFilter.class);

        //JwtAuthenticationFilter 추가
        http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil, customUserDetailsService), UsernamePasswordAuthenticationFilter.class);

        //OAuth2 설정
        http.oauth2Login(oauth2 -> oauth2
                .successHandler(customOAuth2SuccessHandler)
                .failureHandler(customOAuth2FailureHandler)
                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService)));

        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}