package com.duoc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioUpdateDTO {
    @NotBlank @Size(max = 100)
    private String nombres;

    @NotBlank @Size(max = 100)
    private String apellidos;

    @Size(max = 30)
    private String telefono;

    @NotBlank @Size(max = 20)
    private String estado; // ACTIVO/BLOQUEADO/INACTIVO

    public UsuarioUpdateDTO() {}

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
