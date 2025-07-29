package com.nmmc.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.nmmc.auth.serviceImpl.UserDetailsServiceImpl;
import com.nmmc.auth.utils.AuthEntryPointJwt;
import com.nmmc.auth.utils.AuthTokenFilter;
import com.nmmc.auth.utils.JwtRequestFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {

	private final UserDetailsServiceImpl userDetailsService;
	private final AuthEntryPointJwt unauthorizedHandler;

	public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, AuthEntryPointJwt unauthorizedHandler) {
		this.userDetailsService = userDetailsService;
		this.unauthorizedHandler = unauthorizedHandler;
	}

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	public JwtRequestFilter jwtRequestFilter() {
		return new JwtRequestFilter();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable())
				.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.authorizeHttpRequests(authz -> authz.antMatchers("/api/**").permitAll()
						.antMatchers("/auth/**")
						.permitAll().anyRequest().authenticated())
				.addFilterBefore(jwtRequestFilter(),
						UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH");
			}
		};
	}
	

	// @Bean
	// public WebMvcConfigurer corsConfigurer() {
	// return new WebMvcConfigurer() {
	// @Override
	// public void addCorsMappings(CorsRegistry registry) {
	// registry.addMapping("/api/**")
	// .allowedOrigins(
	// "http://localhost:4200",
	// "https://nmmconline.in",
	// "https://app.nmmconline.in",
	// "https://15-206-219-76.nip.io"
	// )
	// .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
	// .allowedHeaders("Authorization", "Cache-Control", "Content-Type")
	// .allowCredentials(true);
	// registry.addMapping("/auth/**")
	// .allowedOrigins(
	// "http://localhost:4200",
	// "https://nmmconline.in",
	// "https://app.nmmconline.in",
	// "https://15-206-219-76.nip.io"
	// )
	// .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
	// .allowedHeaders("Authorization", "Cache-Control", "Content-Type")
	// .allowCredentials(true);
	// }
	// };
	// }
}
