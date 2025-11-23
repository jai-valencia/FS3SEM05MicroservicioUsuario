package com.duoc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioCreateDTO {

    @NotBlank @Size(max = 100)
    private String nombres;

    @NotBlank @Size(max = 100)
    private String apellidos;

    @Email @NotBlank @Size(max = 120)
    private String email;

    @Size(max = 20)
    private String rut;

    @Size(max = 30)
    private String telefono;

    @NotBlank @Size(min = 6, max = 100)
    private String password;

    private String rol;

    public UsuarioCreateDTO() {}

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
