package com.uade.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.ecommerce.dto.UsuarioRegisterDTO;
import com.uade.ecommerce.model.Usuario;
import com.uade.ecommerce.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;


//    @GetMapping()
//    public List<Usuario> getUsuarios() {
//        return usuarioService.getUsuarios();
//    }

    @GetMapping
    public ResponseEntity<List<UsuarioRegisterDTO>> getUsuarios() {
        List<UsuarioRegisterDTO> lista = usuarioService.getUsuarios();
        return ResponseEntity.ok(lista); // Retorna 200 OK
    }

//    @PostMapping
//    public Usuario addUsuario(@RequestBody Usuario usuario) {
//        return usuarioService.crearUsuario(usuario);
//    }

    @PostMapping
    public ResponseEntity<UsuarioRegisterDTO> addUsuario(@RequestBody UsuarioRegisterDTO usuario) {
        UsuarioRegisterDTO nuevo = usuarioService.crearUsuario(usuario);
        return ResponseEntity.ok(nuevo); // Retorna 200 OK
    }

//    @DeleteMapping("/{id}")
//    public void eliminarUsuario(@PathVariable Long id) {
//        usuarioService.eliminarUsuario(id);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }

}
