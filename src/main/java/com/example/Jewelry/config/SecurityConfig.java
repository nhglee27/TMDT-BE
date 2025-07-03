package com.example.Jewelry.config;

import com.example.Jewelry.filter.JwtAuthFilter;
import com.example.Jewelry.service.ServiceImpl.CustomOAuth2FailureHandler;
import com.example.Jewelry.service.ServiceImpl.CustomOAuth2SuccessHandler;
import com.example.Jewelry.service.ServiceImpl.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter authFilter;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    @Autowired
    private CustomOAuth2FailureHandler customOAuth2FailureHandler;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    // authentication
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:3000"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/ws/**",
                                "/api/user/login",
                                "/api/user/register",
                                "/api/user/confirm",
                                "/api/user/resend-confirmation",
                                "/api/product/**",
                                "/api/category/**",
                                "/api/user/info/**",
                                "/api/user/users",
                                "/api/delivery/**",
                                "/api/user/info/**",
                                "/api/user/register-ctv",
                                "/api/user/**",
                                "/oauth2/**",
                                "/api/product/list",
                                "/api/product/{productId}",
                                "/api/product/**",
                                "/api/reverse-auction/**",
                                "/reviews/product/**",
                                "/api/wishlist/**",
                                "/api/cart/**",
                                "/api/product/**",
                                "/api/delivery/fetch-user/**",
                                "/api/delivery/add",
                                "/api/orders/create",
                                "/api/orders/**",
                                "/api/verify/verify-otp",
                                "/api/verify/resend-otp",
                                "/api/product/list",
                                "/api/product/{productId}",
                                "/api/reviews/product/**",
                                "/api/reviews/product/{productId}/average-rating",
                                "/api/reviews/product/{productId}/total-reviews",
                                "/api/category/**",
                                "/api/verify/resend-otp",
                                "/api/payment/create-stripe-session",
                                "/api/webhook/stripe",
                                "/api/upload/**",
                                "/api/auctions/**",
                                "/api/chat/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"error\": \"Unauthorized\"}");
                        })
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(customOAuth2SuccessHandler)
                        .failureHandler(customOAuth2FailureHandler)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder.bCryptPasswordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}

