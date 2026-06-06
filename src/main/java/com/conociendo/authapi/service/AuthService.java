package com.conociendo.authapi.service;

// =====================================================
// Archivo: AuthService.java
// Proyecto: Conociendo.com - API REST
// Descripción: Servicio con la lógica de negocio para registro y login
// Evidencia: GA7-220501096-AA5-EV01
// =====================================================

import com.conociendo.authapi.dto.AuthResponse;
import com.conociendo.authapi.dto.LoginRequest;
import com.conociendo.authapi.dto.RegistroRequest;
import com.conociendo.authapi.model.Usuario;
import com.conociendo.authapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Servicio de autenticación de Conociendo.com.
 *
 * Contiene la lógica de negocio para:
 * 1. Registrar nuevos usuarios
 * 2. Validar credenciales en el inicio de sesión
 *
 * @Service indica a Spring que esta clase es un componente de servicio
 * que puede ser inyectado en otros componentes como el controlador.
 */
@Service
public class AuthService {

    /**
     * Repositorio para acceder a la tabla usuarios en MySQL.
     * @Autowired hace que Spring inyecte el objeto automáticamente.
     */
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Codificador de contraseñas con algoritmo BCrypt.
     * BCrypt es el estándar de la industria para cifrar contraseñas.
     * Convierte "miClave123" en algo como "$2a$12$eImi..."
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    // =====================================================
    // MÉTODO: REGISTRO DE USUARIO
    // =====================================================

    /**
     * Registra un nuevo usuario en el sistema Conociendo.com.
     *
     * Pasos que realiza este método:
     * 1. Verifica que el email no esté ya registrado
     * 2. Cifra la contraseña con BCrypt
     * 3. Guarda el usuario en la base de datos
     * 4. Retorna mensaje de éxito o error
     *
     * @param request objeto con los datos del formulario de registro
     * @return AuthResponse con el resultado del registro
     */
    public AuthResponse registrar(RegistroRequest request) {

        // PASO 1: Verificar si el email ya está registrado
        // Si ya existe, no se puede crear otra cuenta con el mismo email
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return AuthResponse.error(
                "Error en el registro: el email '" + request.getEmail()
                + "' ya está registrado en Conociendo.com."
            );
        }

        // PASO 2: Cifrar la contraseña
        // NUNCA guardamos contraseñas en texto plano por seguridad
        // BCrypt la convierte en un hash irreversible
        String passwordCifrada = passwordEncoder.encode(request.getPassword());

        // PASO 3: Crear el objeto Usuario con los datos del registro
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(request.getNombre());
        nuevoUsuario.setEmail(request.getEmail());
        nuevoUsuario.setPassword(passwordCifrada); // Guardamos el hash, no el texto plano

        // PASO 4: Guardar el usuario en la base de datos MySQL
        usuarioRepository.save(nuevoUsuario);

        // PASO 5: Retornar respuesta de registro exitoso
        return AuthResponse.exitoso(
            nuevoUsuario.getEmail(),
            nuevoUsuario.getNombre(),
            "Registro exitoso. Bienvenida a Conociendo.com, "
            + nuevoUsuario.getNombre() + "!"
        );
    }

    // =====================================================
    // MÉTODO: INICIO DE SESIÓN (LOGIN)
    // =====================================================

    /**
     * Autentica un usuario con su email y contraseña.
     *
     * Pasos que realiza este método:
     * 1. Busca el usuario por email en la base de datos
     * 2. Si no existe → error en la autenticación
     * 3. Compara la contraseña ingresada con el hash guardado
     * 4. Si no coincide → error en la autenticación
     * 5. Si todo es correcto → autenticación satisfactoria
     *
     * @param request objeto con el email y password del formulario de login
     * @return AuthResponse con "Autenticación satisfactoria" o mensaje de error
     */
    public AuthResponse login(LoginRequest request) {

        // PASO 1: Buscar el usuario por email en la base de datos
        Optional<Usuario> usuarioEncontrado = usuarioRepository
                .findByEmail(request.getEmail());

        // PASO 2: Verificar que el usuario existe
        // Si Optional está vacío, no hay ningún usuario con ese email
        if (usuarioEncontrado.isEmpty()) {
            // Nota de seguridad: no decimos "email no existe" para no
            // dar pistas a alguien que intente accesos no autorizados
            return AuthResponse.error(
                "Error en la autenticación: credenciales incorrectas."
            );
        }

        // Obtener el objeto Usuario desde el Optional
        Usuario usuario = usuarioEncontrado.get();

        // PASO 3: Verificar la contraseña
        // passwordEncoder.matches() compara:
        //   - el texto plano que ingresó el usuario ("miClave123")
        //   - con el hash BCrypt guardado en la BD ("$2a$12$eImi...")
        // Retorna true si coinciden, false si no
        boolean passwordCorrecta = passwordEncoder.matches(
            request.getPassword(),   // Contraseña que ingresó el usuario
            usuario.getPassword()    // Hash guardado en la base de datos
        );

        // PASO 4: Si la contraseña no coincide, retornar error
        if (!passwordCorrecta) {
            return AuthResponse.error(
                "Error en la autenticación: credenciales incorrectas."
            );
        }

        // PASO 5: Las credenciales son correctas → autenticación satisfactoria
        return AuthResponse.exitoso(
            usuario.getEmail(),
            usuario.getNombre(),
            "Autenticación satisfactoria. Bienvenida, " + usuario.getNombre() + "!"
        );
    }
}
