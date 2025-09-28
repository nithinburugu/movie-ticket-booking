// package com.example.algobharat;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// public class SecurityConfig {

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http
//                 .csrf(csrf -> csrf.disable()) // disable CSRF (since we're testing with APIs)
//                 .authorizeHttpRequests(auth -> auth
//                         .anyRequest().permitAll() // allow all requests without auth
//                 );
//         return http.build();
//     }
// }

package com.example.algobharat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**", "/api/v1/**")) // Disable CSRF for H2 and
                                                                                            // API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll() // Allow H2 console
                        .requestMatchers("/api/v1/**").permitAll() // Allow all API v1 endpoints
                        .anyRequest().authenticated() // Require auth for other endpoints (adjust later)
                )
                .headers(headers -> headers
                        .addHeaderWriter(
                                new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)) // Allow
                                                                                                                       // iframe
                                                                                                                       // for
                                                                                                                       // H2
                );
        return http.build();
    }
}

// package com.example.algobharat;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import
// org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;
// import
// org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

// @Configuration
// public class SecurityConfig {

// @Bean
// public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
// http
// .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")) // Disable CSRF
// for H2 console
// .authorizeHttpRequests(auth -> auth
// .requestMatchers("/h2-console/**").permitAll() // Allow H2 console
// .requestMatchers("/api/v1/**").permitAll() // Allow API endpoints for testing
// .anyRequest().authenticated() // Require auth for other endpoints (adjust
// later)
// )
// .headers(headers -> headers
// .addHeaderWriter(
// new
// XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
// // Allow
// // iframe
// );
// return http.build();
// }
// }