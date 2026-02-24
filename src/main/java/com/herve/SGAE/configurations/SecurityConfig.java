package com.herve.SGAE.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/auth/register/monitor").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/invoices/").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/invoices/initial/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/invoices/{invoiceId}/mark-as-paid").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/invoices/{invoiceId}/pay-installment").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/invoices/generate-installment-invoice").hasAnyAuthority("ROLE_ADMIN", "ROLE_STUDENT")
                        .requestMatchers("/api/invoices/{invoiceId}/request-payment").hasAuthority("ROLE_STUDENT")
                        .requestMatchers("/api/invoices/{invoiceId}/mark-installment-paid").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/courses/practical").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/vehicles").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/invoices/student/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_STUDENT")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
