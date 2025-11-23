// com/tuapp/usuarios/service/UsuarioService.java
package com.duoc.service;

import com.duoc.model.Rol;
import com.duoc.model.Usuario;
import com.duoc.dto.UsuarioResponse;
import com.duoc.dto.UsuarioUpdateDTO;
import com.duoc.dto.UsuarioCreateDTO;
import com.duoc.dto.LoginRequest;
import com.duoc.repository.RolRepository;
import com.duoc.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final RolRepository rolRepo;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository usuarioRepo, RolRepository rolRepo, PasswordEncoder encoder) {
        this.usuarioRepo = usuarioRepo;
        this.rolRepo = rolRepo;
        this.encoder = encoder;
    }

    public UsuarioResponse crear(UsuarioCreateDTO dto){
        if (usuarioRepo.existsByEmail(dto.getEmail()))
            throw new IllegalArgumentException("El email ya está registrado");
        if (dto.getRut()!=null && !dto.getRut().isBlank() && usuarioRepo.existsByRut(dto.getRut()))
            throw new IllegalArgumentException("El RUT ya está registrado");

        Usuario u = new Usuario();
        u.setNombres(dto.getNombres());
        u.setApellidos(dto.getApellidos());
        u.setEmail(dto.getEmail());
        u.setRut(dto.getRut());
        u.setTelefono(dto.getTelefono());
        u.setHashContrasena(encoder.encode(dto.getPassword()));
        u.setEstado("ACTIVO");
        u.setFechaCreacion(LocalDateTime.now());

        if (dto.getRol()!=null && !dto.getRol().isBlank()){
            Rol rol = rolRepo.findByNombreRol(dto.getRol())
                    .orElseThrow(() -> new IllegalArgumentException("Rol no existente: " + dto.getRol()));
            u.setRoles(Set.of(rol));
        }

        u = usuarioRepo.save(u);
        return toResponse(u);
    }

    public UsuarioResponse actualizar(Long id, UsuarioUpdateDTO dto){
        Usuario u = usuarioRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        u.setNombres(dto.getNombres());
        u.setApellidos(dto.getApellidos());
        u.setTelefono(dto.getTelefono());
        u.setEstado(dto.getEstado());
        return toResponse(u);
    }

    public void eliminar(Long id){
        if (!usuarioRepo.existsById(id)) throw new EntityNotFoundException("Usuario no encontrado");
        usuarioRepo.deleteById(id);
    }

    public UsuarioResponse buscarPorId(Long id){
        return usuarioRepo.findById(id).map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }

    public String login(LoginRequest req){
        Usuario u = usuarioRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Credenciales inválidas"));
        if (!encoder.matches(req.getPassword(), u.getHashContrasena()))
            throw new IllegalArgumentException("Credenciales inválidas");
        // Delega el token al controller (o inyecta JwtService aquí si prefieres)
        return "OK";
    }

    private UsuarioResponse toResponse(Usuario u){
        UsuarioResponse r = new UsuarioResponse();
        r.setId(u.getIdUsuario());
        r.setNombres(u.getNombres());
        r.setApellidos(u.getApellidos());
        r.setEmail(u.getEmail());
        r.setRut(u.getRut());
        r.setTelefono(u.getTelefono());
        r.setEstado(u.getEstado());
        r.setFechaCreacion(u.getFechaCreacion());
        if (u.getRoles()!=null){
            r.setRoles(u.getRoles().stream().map(Rol::getNombreRol).collect(Collectors.toSet()));
        }
        return r;
    }

    public List<UsuarioResponse> listarTodos() {
    return usuarioRepo.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }
}
