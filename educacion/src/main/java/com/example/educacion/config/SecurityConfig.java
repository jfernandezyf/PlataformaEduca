package com.example.educacion.config;

import com.example.educacion.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            .requestMatchers("/v3/api-docs", "/v3/api-docs/**")
            .requestMatchers("/swagger-ui.html", "/swagger-ui/**")
            .requestMatchers("/swagger-resources", "/swagger-resources/**")
            .requestMatchers("/webjars/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos de login y registro
                .requestMatchers("/api/auth/**", "/error").permitAll()
                // Permitir obtener cursos y obtener notas por estudiante a los roles ADMIN, USER y ESTUDIANTE
                .requestMatchers(HttpMethod.GET, "/api/cursos").hasAnyRole("ADMIN", "USER", "ESTUDIANTE")
                .requestMatchers(HttpMethod.GET, "/api/notas/estudiante/**").hasAnyRole("ADMIN", "USER", "ESTUDIANTE")
                // El rol ESTUDIANTE no tiene permitido crear ni realizar otras operaciones en la API
                .requestMatchers("/api/**").hasAnyRole("ADMIN", "USER")
                // Cualquier otra petición al backend requerirá un token válido
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}