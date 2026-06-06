package com.conociendo.authapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistroRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato valido")
    private String email;

    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 6, message = "La contrasena debe tener al menos 6 caracteres")
    private String password;

    public RegistroRequest() {}

    public RegistroRequest(String nombre, String email, String password) {
        this.nombre   = nombre;
        this.email    = email;
        this.password = password;
    }

    public String getNombre()   { return nombre; }
    public String getEmail()    { return email; }
    public String getPassword() { return password; }

    public void setNombre(String nombre)     { this.nombre = nombre; }
    public void setEmail(String email)       { this.email = email; }
    public void setPassword(String password) { this.password = password; }
}