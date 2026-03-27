package com.uade.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.uade.ecommerce.model.Categoria;
import com.uade.ecommerce.service.CategoriaService;

public class CategoriaController {

    @Autowired
    CategoriaService categoriaServ;

    @GetMapping()
    public List<Categoria> getCategorias() {
        return categoriaServ.getCategorias();
    }

    @PostMapping
    public Categoria addCategoria(@RequestBody Categoria categoria) {
        return categoriaServ.crearCategoria(categoria);
    }

    @DeleteMapping("/{id}")
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaServ.eliminarCategoria(id);
    }
    
}
