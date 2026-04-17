package com.uade.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.ecommerce.dto.request.CategoriaRequest;
import com.uade.ecommerce.dto.response.CategoriaResponse;
import com.uade.ecommerce.model.Categoria;
import com.uade.ecommerce.service.CategoriaService;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    CategoriaService categoriaServ;

//    @GetMapping()
//    public List<Categoria> getCategorias() {
//        return categoriaServ.getCategorias();
//    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> getCategorias() {
        List<CategoriaResponse> lista = categoriaServ.getCategorias();
        return ResponseEntity.ok(lista); // Devuelve 200 OK con la lista
    }

//    @PostMapping
//    public Categoria addCategoria(@RequestBody Categoria categoria) {
//        return categoriaServ.crearCategoria(categoria);
//    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> addCategoria(@RequestBody CategoriaRequest request) {
        CategoriaResponse nueva = categoriaServ.crearCategoria(request);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED); // Devuelve 201 Created
    }


//    @DeleteMapping("/{id}")
//    public void eliminarCategoria(@PathVariable Long id) {
//        categoriaServ.eliminarCategoria(id);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriaServ.eliminarCategoria(id);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content
    }
}
