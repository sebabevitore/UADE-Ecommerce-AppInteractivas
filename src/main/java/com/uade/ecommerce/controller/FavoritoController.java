package com.uade.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.ecommerce.dto.response.ProductoResponse;
import com.uade.ecommerce.service.FavoritoService;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> getFavoritos(Authentication authentication) {
        String emailUsuario = authentication.getName();
        List<ProductoResponse> favoritos = favoritoService.getFavoritosByUsuario(emailUsuario);
        return ResponseEntity.ok(favoritos);
    }

    // POST: Agregar un producto a la lista de favoritos
    @PostMapping("/productos/{productoId}")
    public ResponseEntity<List<ProductoResponse>> addProductoToFavoritos(
            @PathVariable Long productoId, 
            Authentication authentication) {
        
        String emailUsuario = authentication.getName();
        List<ProductoResponse> favoritos = favoritoService.addProductoToFavoritos(productoId, emailUsuario);
        return new ResponseEntity<>(favoritos, HttpStatus.CREATED); 
    }

    // DELETE: Eliminar un producto específico de la lista
    @DeleteMapping("/productos/{productoId}")
    public ResponseEntity<List<ProductoResponse>> removeProductoFromFavoritos(
            @PathVariable Long productoId, 
            Authentication authentication) {
        
        String emailUsuario = authentication.getName();
        List<ProductoResponse> favoritos = favoritoService.removeProductoFromFavoritos(productoId, emailUsuario);
        return ResponseEntity.ok(favoritos); // Devuelve la lista actualizada
    }

    @DeleteMapping
    public ResponseEntity<Void> clearFavoritos(Authentication authentication) {
        String emailUsuario = authentication.getName();
        favoritoService.clearFavoritos(emailUsuario);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content
    }
}