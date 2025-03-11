package com.library.management.system.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.library.management.system.service.impl.CustomUserDetailsService;
import com.library.management.system.utils.JwtTokenProvider;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						// Authentication Endpoints
						.requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/auth/refresh", "/api/auth/logout").authenticated()
						.requestMatchers(HttpMethod.GET, "/api/auth/me").authenticated()

						// User Management Endpoints (Only ADMIN can access)
						.requestMatchers(HttpMethod.GET, "/api/users", "/api/users/{id}").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/api/users/{id}", "/api/users/{id}/role").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")

						// Book Endpoints
						.requestMatchers(HttpMethod.GET, "/api/books", "/api/books/{id}").authenticated()
						.requestMatchers(HttpMethod.POST, "/api/books").hasAnyRole("ADMIN", "LIBRARIAN")
						.requestMatchers(HttpMethod.PUT, "/api/books/{id}").hasAnyRole("ADMIN", "LIBRARIAN")
						.requestMatchers(HttpMethod.DELETE, "/api/books/{id}").hasRole("ADMIN")

						// Author Endpoints
						.requestMatchers(HttpMethod.GET, "/api/authors", "/api/authors/{id}").authenticated()
						.requestMatchers(HttpMethod.POST, "/api/authors").hasAnyRole("ADMIN", "LIBRARIAN")
						.requestMatchers(HttpMethod.PUT, "/api/authors/{id}").hasAnyRole("ADMIN", "LIBRARIAN")
						.requestMatchers(HttpMethod.DELETE, "/api/authors/{id}").hasRole("ADMIN")

						// Publicly accessible API documentation
						.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

						// H2 Console for development (should be disabled in production)
						.requestMatchers("/h2-console/**").permitAll()

						// Any other requests must be authenticated
						.anyRequest().authenticated())
				.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService),
						UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
