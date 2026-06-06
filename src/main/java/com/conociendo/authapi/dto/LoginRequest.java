package com.conociendo.authapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato valido")
    private String email;

    @NotBlank(message = "La contrasena es obligatoria")
    private String password;

    public LoginRequest() {}

    public LoginRequest(String email, String password) {
        this.email    = email;
        this.password = password;
    }

    public String getEmail()    { return email; }
    public String getPassword() { return password; }

    public void setEmail(String email)       { this.email = email; }
    public void setPassword(String password) { this.password = password; }
}