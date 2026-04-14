package com.uade.ecommerce.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uade.ecommerce.model.Categoria;
import com.uade.ecommerce.repository.CategoriaRepository;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository repo;

    public List<Categoria> getCategorias() {
        return repo.findAll();
    }

    public Categoria crearCategoria(Categoria categoria) {
        repo.save(categoria);
        return categoria;
    }

    public void eliminarCategoria(Long id) {
        if  (repo.existsById(id)){
            repo.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("No existe la categoria con el id: " + id);
        }
    }

}
