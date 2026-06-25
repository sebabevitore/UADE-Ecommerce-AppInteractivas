package com.uade.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecommerce.dto.response.ProductoResponse;
import com.uade.ecommerce.exception.ProductoNotFoundException;
import com.uade.ecommerce.exception.UsuarioNotFoundException;
import com.uade.ecommerce.exception.FavoriteNotFoundException;
import com.uade.ecommerce.model.Favorito;
import com.uade.ecommerce.model.Producto;
import com.uade.ecommerce.model.Usuario;
import com.uade.ecommerce.repository.FavoritoRepository;
import com.uade.ecommerce.repository.ProductoRepository;
import com.uade.ecommerce.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private ProductoRepository productoRepo;

    private Favorito obtenerFavoritoValidado(String emailUsuario) {
        Usuario usuario = usuarioRepo.findByEmail(emailUsuario)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));
                
        Favorito favorito = usuario.getFavorito();
        if (favorito == null) {
            throw new FavoriteNotFoundException("Favorito no encontrado para el usuario");
        }
        return favorito;
    }

    // 1. Obtener la lista de favoritos
    @Transactional
    public List<ProductoResponse> getFavoritosByUsuario(String emailUsuario) {
        Favorito favorito = obtenerFavoritoValidado(emailUsuario);
        return convertToResponseList(favorito.getProductos());
    }

    // 2. Agregar un item a favoritos
    @Transactional
    public List<ProductoResponse> addProductoToFavoritos(Long idProducto, String emailUsuario) {
        Favorito favorito = obtenerFavoritoValidado(emailUsuario);

        Producto p = productoRepo.findById(idProducto)
                    .orElseThrow(() -> new ProductoNotFoundException("No existe el producto con el id: " + idProducto));

        // Verificamos que no esté ya agregado para evitar duplicados en la lista
        boolean yaExiste = favorito.getProductos().stream()
                .anyMatch(prod -> prod.getId_prod().equals(idProducto));

        if (!yaExiste) {
            favorito.getProductos().add(p);
            favoritoRepo.save(favorito);
        }

        return convertToResponseList(favorito.getProductos());
    }

    // 3. Remover un item de favoritos
    @Transactional
    public List<ProductoResponse> removeProductoFromFavoritos(Long idProducto, String emailUsuario) {
        Favorito favorito = obtenerFavoritoValidado(emailUsuario);

        boolean removido = favorito.getProductos().removeIf(p -> p.getId_prod().equals(idProducto));
        
        if (!removido) {
            throw new ProductoNotFoundException("El producto no estaba en tu lista de favoritos");
        }

        favoritoRepo.save(favorito);
        
        // Devolvemos la lista actualizada tras el borrado
        return convertToResponseList(favorito.getProductos());
    }

    // 4. Vaciar la lista completa (Manual)
    @Transactional
    public void clearFavoritos(String emailUsuario) {
        Favorito favorito = obtenerFavoritoValidado(emailUsuario);
        favorito.getProductos().clear();
        favoritoRepo.save(favorito);
    }

    // Método privado para mapear la lista de Productos a DTOs usando tu ProductoResponse
    private List<ProductoResponse> convertToResponseList(List<Producto> productos) {
        return productos.stream().map(p -> {
            List<String> nombresCategorias = p.getCategorias().stream()
                    .map(cat -> cat.getNombre())
                    .toList();

            return ProductoResponse.builder()
                    .id(p.getId_prod())
                    .nombre(p.getNombre())
                    .descripcion(p.getDescripcion())
                    .precio(p.getPrecio())
                    .stock(p.getStock())
                    .categorias(nombresCategorias.isEmpty() ? List.of("Sin categoría") : nombresCategorias)
                    .imagenUrl(p.getImagenUrl())
                    .freeShipping(p.isFreeShipping())
                    .isPromo(p.isPromo())
                    .build();
        }).toList();
    }
}