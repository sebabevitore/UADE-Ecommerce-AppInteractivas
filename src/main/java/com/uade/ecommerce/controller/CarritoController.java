package com.uade.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication; //libreria de Spring


import com.uade.ecommerce.dto.request.ItemCarritoRequest;
import com.uade.ecommerce.dto.response.CarritoResponse;
import com.uade.ecommerce.service.CarritoService;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {
    @Autowired
    private CarritoService carritoService;

    // GET carrito del usuario autenticado
    @GetMapping
    public ResponseEntity<CarritoResponse> getCarrito(Authentication authentication) {
        String emailUsuario = authentication.getName();
        CarritoResponse carrito = carritoService.getCarritoByUsuario(emailUsuario);
        return ResponseEntity.ok(carrito);
    }

    // POST agregar item al carrito
    @PostMapping("/items")
    public ResponseEntity<CarritoResponse> addItemToCart(@RequestBody ItemCarritoRequest request, Authentication authentication) {
        String emailUsuario = authentication.getName();
        CarritoResponse carrito = carritoService.addItemToCart(request, emailUsuario);
        return new ResponseEntity<>(carrito, HttpStatus.CREATED);
    }

    // PUT actualizar cantidad de un item
    @PutMapping("/items/{itemId}")
    public ResponseEntity<CarritoResponse> updateItemQuantity(
            @PathVariable Long itemId,
            @RequestBody ItemCarritoRequest request,
            Authentication authentication //objeto que contiene toda la información del usuario que está haciendo la petición en ese momento exacto.
        ) {
        String email = authentication.getName();
        CarritoResponse carrito = carritoService.updateItemQuantity(itemId, request.getCantidad(), email);
        return ResponseEntity.ok(carrito);
    }


    // DELETE eliminar un item del carrito
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CarritoResponse> removeItemFromCart(@PathVariable Long itemId, Authentication authentication) {
        String emailUsuario = authentication.getName();
        CarritoResponse carrito = carritoService.removeItemFromCart(itemId, emailUsuario);
        return ResponseEntity.ok(carrito);
    }

    // DELETE vaciar carrito
    @DeleteMapping
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        String emailUsuario = authentication.getName();
        carritoService.clearCart(emailUsuario);
        return ResponseEntity.noContent().build();
    }

    // POST realizar checkout (convertir carrito a pedido)
    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(Authentication authentication) {
        String emailUsuario = authentication.getName();
        carritoService.checkoutCarrito(emailUsuario);
        return ResponseEntity.ok("Pedido creado exitosamente");
    }
}
