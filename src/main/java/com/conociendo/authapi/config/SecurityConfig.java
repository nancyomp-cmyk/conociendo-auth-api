package com.conociendo.authapi.config;

// =====================================================
// Archivo: SecurityConfig.java
// Proyecto: Conociendo.com - API REST
// Descripción: Configuración de seguridad para el servicio web REST
// Evidencia: GA7-220501096-AA5-EV01
// =====================================================

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de Spring Security para la API REST.
 *
 * En un API REST la seguridad funciona diferente a una aplicación web normal:
 * - NO hay páginas de login HTML
 * - NO hay sesiones en el servidor
 * - La autenticación se hace enviando JSON con email y contraseña
 * - Las respuestas siempre son JSON, nunca HTML
 *
 * @Configuration indica que esta clase contiene configuración de Spring
 * @EnableWebSecurity activa la configuración personalizada de seguridad
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura qué rutas (URLs) del API son públicas
     * y cuáles requieren autenticación.
     *
     * Para este servicio web, TODOS los endpoints de /auth/ son públicos
     * porque son precisamente los endpoints de registro y login.
     *
     * @param http objeto de configuración HTTP de Spring Security
     * @return cadena de filtros de seguridad configurada
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
            // Deshabilitar CSRF (protección contra ataques web)
            // En APIs REST se deshabilita porque no usamos formularios HTML
            .csrf(csrf -> csrf.disable())

            // Configurar el API como SIN ESTADO (stateless)
            // Cada petición HTTP es independiente, no hay sesiones
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Definir qué rutas son públicas y cuáles requieren login
            .authorizeHttpRequests(auth -> auth
                // /auth/** incluye /auth/registro, /auth/login, /auth/estado
                // Todas son públicas: cualquiera puede acceder sin autenticarse
                .requestMatchers("/auth/**").permitAll()
                // Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            );

        return http.build();
    }

    /**
     * Define el algoritmo para cifrar y verificar contraseñas.
     *
     * BCrypt es el algoritmo recomendado para contraseñas:
     * - Es unidireccional: no se puede "descifrar" una contraseña
     * - Agrega "sal" aleatoria: dos contraseñas iguales generan hashes distintos
     * - strength=12 significa 2^12 = 4096 iteraciones (más seguro, un poco más lento)
     *
     * @return instancia del codificador BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}