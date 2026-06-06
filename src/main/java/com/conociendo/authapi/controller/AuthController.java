package com.conociendo.authapi.controller;

// =====================================================
// Archivo: AuthController.java
// Proyecto: Conociendo.com - API REST
// Descripcion: Controlador REST que expone los endpoints
//              de registro e inicio de sesion
// Evidencia: GA7-220501096-AA5-EV01
// Aprendiz: Nancy Mosquera
// =====================================================

import com.conociendo.authapi.dto.AuthResponse;
import com.conociendo.authapi.dto.LoginRequest;
import com.conociendo.authapi.dto.RegistroRequest;
import com.conociendo.authapi.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST de autenticacion para Conociendo.com.
 *
 * Expone estos endpoints (URLs del servicio web):
 *
 *   POST http://localhost:8081/api/auth/registro
 *        Para registrar un nuevo usuario
 *
 *   POST http://localhost:8081/api/auth/login
 *        Para iniciar sesion
 *
 *   GET  http://localhost:8081/api/auth/estado
 *        Para verificar que el servicio esta activo
 *
 * @RestController indica que esta clase recibe y responde
 *                 peticiones HTTP en formato JSON
 * @RequestMapping define el prefijo /auth para todas las URLs
 * @CrossOrigin permite peticiones desde cualquier dominio
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    /**
     * Servicio de autenticacion inyectado por Spring.
     * Contiene toda la logica de negocio para registro y login.
     */
    @Autowired
    private AuthService authService;

    // =====================================================
    // ENDPOINT 1: REGISTRO
    // URL: POST http://localhost:8081/api/auth/registro
    // =====================================================

    /**
     * Registra un nuevo usuario en Conociendo.com.
     *
     * El cliente envia este JSON:
     * {
     *   "nombre": "Nancy Mosquera",
     *   "email": "nancy@email.com",
     *   "password": "miClave123"
     * }
     *
     * Respuesta exitosa (HTTP 201):
     * { "exitoso": true, "mensaje": "Registro exitoso...", ... }
     *
     * Respuesta de error (HTTP 409):
     * { "exitoso": false, "mensaje": "Error en el registro...", ... }
     *
     * @param request datos del nuevo usuario
     * @return ResponseEntity con AuthResponse en formato JSON
     */
    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registro(
            @Valid @RequestBody RegistroRequest request) {

        // Llamar al servicio para procesar el registro
        AuthResponse respuesta = authService.registrar(request);

        if (respuesta.isExitoso()) {
            // HTTP 201 Created: usuario registrado correctamente
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } else {
            // HTTP 409 Conflict: el email ya esta registrado
            return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
        }
    }

    // =====================================================
    // ENDPOINT 2: LOGIN (INICIO DE SESION)
    // URL: POST http://localhost:8081/api/auth/login
    // =====================================================

    /**
     * Autentica un usuario con email y contrasena.
     *
     * El cliente envia este JSON:
     * {
     *   "email": "nancy@email.com",
     *   "password": "miClave123"
     * }
     *
     * Si la autenticacion es correcta (HTTP 200):
     * { "exitoso": true, "mensaje": "Autenticacion satisfactoria", ... }
     *
     * Si las credenciales son incorrectas (HTTP 401):
     * { "exitoso": false, "mensaje": "Error en la autenticacion...", ... }
     *
     * @param request email y contrasena del usuario
     * @return ResponseEntity con AuthResponse y codigo HTTP
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        // Llamar al servicio para verificar las credenciales
        AuthResponse respuesta = authService.login(request);

        if (respuesta.isExitoso()) {
            // HTTP 200 OK: autenticacion satisfactoria
            return ResponseEntity.ok(respuesta);
        } else {
            // HTTP 401 Unauthorized: error en la autenticacion
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        }
    }

    // =====================================================
    // ENDPOINT 3: ESTADO DEL SERVICIO
    // URL: GET http://localhost:8081/api/auth/estado
    // =====================================================

    /**
     * Verifica que el servicio web esta activo y funcionando.
     * Util para comprobar que el API responde correctamente.
     *
     * @return mensaje de confirmacion en texto plano
     */
    @GetMapping("/estado")
    public ResponseEntity<String> estado() {
        return ResponseEntity.ok(
            "Servicio web de Conociendo.com activo en puerto 8081"
        );
    }
}