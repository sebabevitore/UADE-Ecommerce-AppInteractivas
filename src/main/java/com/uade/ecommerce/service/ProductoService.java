package com.uade.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecommerce.model.Producto;
import com.uade.ecommerce.repository.ProductoRepository;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository repo;

    public List<Producto> getProductos() {
        return repo.findAll();
    }

    public Producto crearProducto(Producto producto) {
        repo.save(producto);
        return producto;
    }

    public void eliminarProducto(Long id) {
        if  (repo.existsById(id)){
            repo.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("No existe el producto con el id: " + id);
        }
    }



}
