package com.tasktracker.userservice.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tasktracker.userservice.jwt.JwtAuthenticationEntryPoint;
import com.tasktracker.userservice.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityFilterConfig {
	@Autowired
	private JwtAuthenticationEntryPoint point;
	@Autowired
	private JwtAuthenticationFilter filter;
	String[] admin = { "/user/createUser", "/user/getUsersById", "/user/getUserByDate", "/user/updateUser",
			"/user/deleteUser", "/user/upload", "/user/login", "/user/verifyOtp" };

	String[] user = { "/user/createUser", "/user/getUsersById", "/user/updateUser", "/user/login", "/user/verifyOtp" };

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
		return security.csrf(csrf -> csrf.disable()).cors(cors -> cors.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers("/authenticate").permitAll()
						.requestMatchers("/register").permitAll()
						.requestMatchers(admin).hasAuthority("ADMIN")
						.requestMatchers(user).hasAuthority("USER")
						.requestMatchers("/user/getUsersById").hasAuthority("MANAGER")
						.requestMatchers("/user/getUserByDate").hasAuthority("HR")		
						.anyRequest()
						.authenticated())
				.exceptionHandling(ex -> ex.authenticationEntryPoint(point))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class).build();
	}

}
