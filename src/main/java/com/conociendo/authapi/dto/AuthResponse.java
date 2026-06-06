package com.conociendo.authapi.dto;

// =====================================================
// Archivo: AuthResponse.java
// Proyecto: Conociendo.com - API REST
// Descripcion: DTO de respuesta para los endpoints de autenticacion
// Evidencia: GA7-220501096-AA5-EV01
// =====================================================

/**
 * Clase que modela la respuesta JSON que el API envia al cliente.
 *
 * Respuesta exitosa:
 * { "exitoso": true, "mensaje": "Autenticacion satisfactoria",
 *   "email": "nancy@email.com", "nombre": "Nancy Mosquera" }
 *
 * Respuesta de error:
 * { "exitoso": false, "mensaje": "Error en la autenticacion...",
 *   "email": null, "nombre": null }
 */
public class AuthResponse {

    /** true si la operacion fue exitosa, false si hubo error */
    private boolean exitoso;

    /** Mensaje descriptivo del resultado */
    private String mensaje;

    /** Email del usuario autenticado (null si hubo error) */
    private String email;

    /** Nombre del usuario autenticado (null si hubo error) */
    private String nombre;

    /** Constructor vacio requerido por Jackson para convertir a JSON */
    public AuthResponse() {}

    /** Constructor con todos los campos */
    public AuthResponse(boolean exitoso, String mensaje,
                        String email, String nombre) {
        this.exitoso = exitoso;
        this.mensaje = mensaje;
        this.email   = email;
        this.nombre  = nombre;
    }

    // Getters - necesarios para que Jackson serialice el objeto a JSON
    public boolean isExitoso() { return exitoso; }
    public String getMensaje()  { return mensaje; }
    public String getEmail()    { return email; }
    public String getNombre()   { return nombre; }

    // Setters
    public void setExitoso(boolean exitoso) { this.exitoso = exitoso; }
    public void setMensaje(String mensaje)  { this.mensaje = mensaje; }
    public void setEmail(String email)      { this.email = email; }
    public void setNombre(String nombre)    { this.nombre = nombre; }

    /**
     * Metodo de utilidad para crear una respuesta exitosa.
     * @param email   email del usuario autenticado
     * @param nombre  nombre del usuario autenticado
     * @param mensaje mensaje de exito personalizado
     * @return AuthResponse con exitoso = true
     */
    public static AuthResponse exitoso(String email,
                                        String nombre,
                                        String mensaje) {
        return new AuthResponse(true, mensaje, email, nombre);
    }

    /**
     * Metodo de utilidad para crear una respuesta de error.
     * @param mensaje descripcion del error ocurrido
     * @return AuthResponse con exitoso = false y sin datos de usuario
     */
    public static AuthResponse error(String mensaje) {
        return new AuthResponse(false, mensaje, null, null);
    }
}