package com.driver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/drivers/*/location").hasRole("write").anyRequest()
						.authenticated())
				.oauth2ResourceServer(
						oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(this.jwtAuthenticationConverter())));

		return http.build();
	}

	/**
	 * Extrae los roles de Keycloak desde "realm_access.roles" y los convierte a
	 * GrantedAuthority con prefijo ROLE_ que espera Spring Security.
	 */
	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		converter.setJwtGrantedAuthoritiesConverter(jwt -> {
			final Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
			if (realmAccess == null || !realmAccess.containsKey("roles")) {
				return List.of();
			}
			@SuppressWarnings("unchecked")
			final List<String> roles = (List<String>) realmAccess.get("roles");
			final Collection<GrantedAuthority> authorities = roles.stream()
					.map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
			return authorities;
		});
		return converter;
	}
}
