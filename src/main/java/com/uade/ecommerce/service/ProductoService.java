package com.uade.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecommerce.dto.request.ProductoRequest;
import com.uade.ecommerce.dto.response.ProductoResponse;
import com.uade.ecommerce.exception.ProductoNotFoundException;
import com.uade.ecommerce.model.Categoria;
import com.uade.ecommerce.model.Producto;
import com.uade.ecommerce.repository.CategoriaRepository;
import com.uade.ecommerce.repository.ProductoRepository;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository repo;

    @Autowired
    private CategoriaRepository categoriaRepo;

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
        
        // BUSCAMOS LAS CATEGORÍAS POR ID
        if (request.getCategoriaIds() != null && !request.getCategoriaIds().isEmpty()) {
            List<Categoria> categoriasExistentes = categoriaRepo.findAllById(request.getCategoriaIds());
            producto.setCategorias(categoriasExistentes);
        }
            
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
            throw new ProductoNotFoundException("No existe el producto con el id: " + id);
        }
    }

    public ProductoResponse updateProducto(Long id, ProductoRequest request) {
        // 1. Buscamos si existe
        Producto productoExistente = repo.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("No existe el producto con el id: " + id));

        // 2. Mapeamos los datos del DTO a la Entidad
        productoExistente.setNombre(request.getNombre());
        productoExistente.setDescripcion(request.getDescripcion());
        productoExistente.setPrecio(request.getPrecio());
        
        // 3. Guardamos y convertimos a respuesta
        Producto actualizado = repo.save(productoExistente);
        return convertToDTO(actualizado);
    }

    private ProductoResponse convertToDTO(Producto p) {
        // Transformamos la lista de objetos Categoria en una lista de Strings (sus nombres)
        List<String> nombresCategorias = p.getCategorias().stream()
                .map(cat -> cat.getNombre())
                .toList();

        return ProductoResponse.builder()
                .id(p.getId_prod())
                .nombre(p.getNombre())
                .descripcion(p.getDescripcion())
                .precio(p.getPrecio())
                // Si la lista está vacía, devolvemos una lista con "Sin categoría" o vacía
                .categorias(nombresCategorias.isEmpty() ? List.of("Sin categoría") : nombresCategorias)
                .build();
    }
    



}
