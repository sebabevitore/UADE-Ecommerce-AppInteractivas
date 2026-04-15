package com.uade.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping()
    public List<Producto> getProductos() {
        return prodService.getProductos();
    }

    @PostMapping
    public Producto addProducto(@RequestBody Producto producto) {
        return prodService.crearProducto(producto);
    }

    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id) {
        prodService.eliminarProducto(id);
    }


}
