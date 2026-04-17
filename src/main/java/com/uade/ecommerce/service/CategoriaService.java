package com.uade.ecommerce.service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecommerce.dto.request.CategoriaRequest;
import com.uade.ecommerce.dto.response.CategoriaResponse;
import com.uade.ecommerce.model.Categoria;
import com.uade.ecommerce.repository.CategoriaRepository;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository repo;

//    public List<CategoriaResponse> getCategorias() {
//        return repo.findAll();
//    }

    public List<CategoriaResponse> getCategorias() {
        // 1. Buscamos las entidades en la DB
        List<Categoria> categorias = repo.findAll();
        
        // 2. Las transformamos a CategoriaResponse usando Stream
        return categorias.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

//    public CategoriaResponse crearCategoria(CategoriaRequest request) {
//        repo.save(request);
//        return request;
//    }

    public CategoriaResponse crearCategoria(CategoriaRequest request) {
        // 1. Convertimos el Request (DTO) a Entidad para que JPA la entienda
        Categoria categoria = new Categoria();
        categoria.setNombre(request.getNombre());

        // 2. Guardamos la entidad
        Categoria guardada = repo.save(categoria);

        // 3. Devolvemos el DTO con el ID generado
        return convertToDTO(guardada);
    }

//    public void eliminarCategoria(Long id) {
//        if  (repo.existsById(id)){
//            repo.deleteById(id);
//      }
//        else {
//            throw new IllegalArgumentException("No existe la categoria con el id: " + id);
//        }
//    }

    public void eliminarCategoria(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        } else {
            throw new IllegalArgumentException("No existe la categoria con el id: " + id);
        }
    }

    private CategoriaResponse convertToDTO(Categoria entidad) {
        CategoriaResponse response = new CategoriaResponse();
        // Ojo acá: usá el nombre exacto de tu atributo en la entidad Categoria
        // Si lo dejaste como id_categoria, usá ese.
        response.setId(entidad.getId_categoria()); 
        response.setNombre(entidad.getNombre());
        return response;
    }
}
