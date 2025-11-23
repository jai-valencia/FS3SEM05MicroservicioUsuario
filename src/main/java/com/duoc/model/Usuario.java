// Usuario con Lombok
package com.duoc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name="USUARIOS")
@Getter @Setter
public class Usuario {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ID_USUARIO")
    private Long idUsuario;

    @Column(name="NOMBRES", nullable=false, length=100) private String nombres;
    @Column(name="APELLIDOS", nullable=false, length=100) private String apellidos;
    @Column(name="EMAIL", nullable=false, unique=true, length=120) private String email;
    @Column(name="RUT", unique=true, length=20) private String rut;
    @Column(name="TELEFONO", length=30) private String telefono;
    @Column(name="HASH_CONTRASENA", nullable=false, length=200) private String hashContrasena;
    @Column(name="ESTADO", nullable=false, length=20) private String estado;
    @Column(name="FECHA_CREACION", nullable=false) private LocalDateTime fechaCreacion;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="USUARIOS_ROLES",
            joinColumns=@JoinColumn(name="ID_USUARIO"),
            inverseJoinColumns=@JoinColumn(name="ID_ROL"))
    private Set<Rol> roles;
}
