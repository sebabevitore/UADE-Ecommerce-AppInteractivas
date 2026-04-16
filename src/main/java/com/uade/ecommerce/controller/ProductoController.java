package com.uade.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.ecommerce.dto.ProductoRequest;
import com.uade.ecommerce.dto.ProductoResponse;
import com.uade.ecommerce.model.Producto;
import com.uade.ecommerce.service.ProductoService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    @Autowired
    ProductoService prodService;

//    @GetMapping()
//    public List<Producto> getProductos() {
//        return prodService.getProductos();
//    }

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> getProductos() {
        List<ProductoResponse> lista = prodService.getProductos();
        return ResponseEntity.ok(lista); // Retorna 200 OK
    }

//    @PostMapping
//    public Producto addProducto(@RequestBody Producto producto) {
//        return prodService.crearProducto(producto);
//    }

    @PostMapping
    public ResponseEntity<ProductoResponse> addProducto(@RequestBody ProductoRequest productoRequest) {
        ProductoResponse nuevo = prodService.crearProducto(productoRequest);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED); // Retorna 201 Created
    }

//    @DeleteMapping("/{id}")
//    public void eliminarProducto(@PathVariable Long id) {
//        prodService.eliminarProducto(id);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        prodService.eliminarProducto(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }


}
