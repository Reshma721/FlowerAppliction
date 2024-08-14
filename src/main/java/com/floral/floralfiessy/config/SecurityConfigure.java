package com.floral.floralfiessy.config;

import com.floral.floralfiessy.filter.JwtAuthenticationFilter;
import com.floral.floralfiessy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigure {

        @Autowired
        private UserService userService;

        @Autowired
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/loginregister/**").permitAll()
                            .requestMatchers("/user/create","/user/update/{id}","/user/delete/{id}").hasAuthority("USER")
                            .requestMatchers("/user/getAll","/user/getById/{id}").hasAuthority("ADMIN")
                            .requestMatchers("/categories/getAllCategory","/categories/getCategoryById/{id}").hasAuthority("USER")
                            .requestMatchers("/categories/addCategory","/categories/updateCategory/{id}","/categories/deleteCategory/{id}").hasAuthority("ADMIN")
                            .requestMatchers("/products/getAllProducts","/products/getProductById/{id}").hasAuthority("USER")
                            .requestMatchers("/products/addProduct","/products/updateProduct/{id}","/products/deleteProduct/{id}").hasAuthority("ADMIN")
                            .requestMatchers("/carts/**").hasAuthority("USER")
                            .requestMatchers("/orders/getAllOrders","/orders/getOrderById/{id}").hasAuthority("ADMIN")
                            .requestMatchers("/orders/addOrder","/orders/updateOrder/{id}","/orders/deleteOrder/{id}","/orders/getOrderById/{id}").hasAuthority("USER")
                            .requestMatchers("/payments/getAllPayments","/payments/getPaymentById/{id}").hasAuthority("ADMIN")
                            .requestMatchers("/payments/addPayment","/payments/updatePayment/{id}","/payments/deletePayment/{id}").hasAuthority("USER")
                            .requestMatchers("/reviews/getReviewById/{id}").hasAuthority("ADMIN")
                            .requestMatchers("/reviews/getAllReviews","/reviews/addReview","/reviews/updateReview/{id}","/reviews/deleteReview/{id}").hasAuthority("USER")
                            .requestMatchers("/user/delete/{id}","/categories/getAllCategory","/products/getAllProducts","/orders/deleteOrder/{id}","/reviews/getAllReviews").hasAuthority("ADMIN")
                            .anyRequest().authenticated())
                    .userDetailsService(userService)
                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
            return configuration.getAuthenticationManager();
        }
    }

