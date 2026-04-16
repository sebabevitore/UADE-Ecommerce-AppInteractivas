package com.uade.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecommerce.dto.ProductoRequest;
import com.uade.ecommerce.dto.ProductoResponse;
import com.uade.ecommerce.exception.ProductoNotFoundException;
import com.uade.ecommerce.model.Producto;
import com.uade.ecommerce.repository.ProductoRepository;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository repo;

//    public List<Producto> getProductos() {
//        return repo.findAll();
//    }

    public List<ProductoResponse> getProductos() {
        return repo.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

//    public Producto crearProducto(Producto producto) {
//        repo.save(producto);
//        return producto;
//    }

    public ProductoResponse crearProducto(ProductoRequest request) {
        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
            
        Producto guardado = repo.save(producto);
        return convertToDTO(guardado);
    }

//    public void eliminarProducto(Long id) {
//        if  (repo.existsById(id)){
//            repo.deleteById(id);
//        }
//        else {
//            throw new IllegalArgumentException("No existe el producto con el id: " + id);
//        }

    public void eliminarProducto(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        } else {
            // Luego cambiaremos esto por tu ProductoNotFoundException personalizada
            throw new ProductoNotFoundException("No existe el producto con el id: " + id);
        }
    }

    private ProductoResponse convertToDTO(Producto p) {
        String nombreCat = "Sin categoría";
        if (!p.getCategorias().isEmpty()) {
            nombreCat = p.getCategorias().get(0).getNombre();
        }
        
        return ProductoResponse.builder()
                .id(p.getId_prod())
                .nombre(p.getNombre())
                .descripcion(p.getDescripcion())
                .precio(p.getPrecio())
                .categoria(nombreCat)
                .build();
    }
    



}
