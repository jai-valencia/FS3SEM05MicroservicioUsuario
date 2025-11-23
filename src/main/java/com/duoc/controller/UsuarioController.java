// com/duoc/usuarios/controller/UsuarioController.java
package com.duoc.controller;

import com.duoc.dto.UsuarioCreateDTO;
import com.duoc.dto.UsuarioUpdateDTO;
import com.duoc.dto.UsuarioResponse;
import com.duoc.dto.LoginRequest;
import com.duoc.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // POST /api/usuarios  (crear)
    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioCreateDTO dto){
        UsuarioResponse r = service.crear(dto);
        return ResponseEntity.created(URI.create("/api/usuarios/" + r.getId())).body(r);
    }

    // ✅ GET /api/usuarios  (listar)  <-- necesario para evitar 405 en tu prueba
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar(){
        return ResponseEntity.ok(service.listarTodos());
    }

    // ✅ GET /api/usuarios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> porId(@PathVariable Long id){
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // PUT /api/usuarios/{id}
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizar(@PathVariable Long id,
                                                      @Valid @RequestBody UsuarioUpdateDTO dto){
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    // DELETE /api/usuarios/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // POST /api/usuarios/login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest req){
        return ResponseEntity.ok(service.login(req));
    }
}
