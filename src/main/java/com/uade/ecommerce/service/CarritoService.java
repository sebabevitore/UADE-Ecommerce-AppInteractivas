package com.uade.ecommerce.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.uade.ecommerce.dto.request.ItemCarritoRequest;
import com.uade.ecommerce.dto.request.ItemPedidoRequest;
import com.uade.ecommerce.dto.request.PedidoRequest;
import com.uade.ecommerce.dto.response.CarritoResponse;
import com.uade.ecommerce.dto.response.ItemCarritoResponse;
import com.uade.ecommerce.exception.CantidadInvalidaException;
import com.uade.ecommerce.exception.CarritoNotFoundException;
import com.uade.ecommerce.exception.CarritoVacioException;
import com.uade.ecommerce.exception.ItemCarritoNotFoundException;
import com.uade.ecommerce.exception.ProductoNotFoundException;
import com.uade.ecommerce.exception.StockInsuficienteException;
import com.uade.ecommerce.exception.UsuarioNotFoundException;
import com.uade.ecommerce.model.Carrito;
import com.uade.ecommerce.model.ItemCarrito;
import com.uade.ecommerce.model.Producto;
import com.uade.ecommerce.model.Usuario;
import com.uade.ecommerce.repository.CarritoRepository;
import com.uade.ecommerce.repository.ProductoRepository;
import com.uade.ecommerce.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class CarritoService {
    @Autowired
    private CarritoRepository carritoRepo;
    @Autowired
    private ProductoRepository productoRepo;
    @Autowired
    private UsuarioRepository usuarioRepo;
    @Autowired
    private PedidoService pedidoService;


    private Carrito obtenerCarritoValidado(String emailUsuario) {
        Usuario usuario = usuarioRepo.findByEmail(emailUsuario)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));
                
        Carrito carrito = usuario.getCarrito();
        if (carrito == null) {
            throw new CarritoNotFoundException("Carrito no encontrado para el usuario");
        }
        return carrito;
    }

    @Transactional
    public CarritoResponse getCarritoByUsuario(String emailUsuario) {
        Carrito carrito = obtenerCarritoValidado(emailUsuario);
        return convertToCarritoResponse(carrito);
    }
    //no va a tener creacion de carrito porque se crea desde la creacion de usuario.


    //agregar items al carrito
    @Transactional
    public CarritoResponse addItemToCart(ItemCarritoRequest request, String emailUsuario) {
        
        //1. valida cantidad
        if (request.getCantidad() <= 0) {
            throw new CantidadInvalidaException("La cantidad debe ser mayor a 0");
        }

        Carrito carrito = obtenerCarritoValidado(emailUsuario);

        //busca producto
        Producto p = productoRepo.findById(request.getIdProducto())
                    .orElseThrow(() -> new ProductoNotFoundException(request.getIdProducto()));

        //valida que haya stock para vender
        if (!validarStock(p, request.getCantidad())) {
            throw new CantidadInvalidaException("Stock insuficiente para agregar esta cantidad.");
        }

        ItemCarrito itemExistente = null;
        for(ItemCarrito item : carrito.getItems()) {
            if(item.getProducto().getId_prod().equals(request.getIdProducto())) {
                itemExistente = item;
                break;
            }
        }

        if(itemExistente != null) {
            int cantidadTotal = itemExistente.getCantidad() + request.getCantidad();
            if(!validarStock(p, cantidadTotal)) {
                throw new StockInsuficienteException("La cantidad total en el carrito supera el stock disponible.");
            }
            itemExistente.setCantidad(cantidadTotal);
            itemExistente.setPrecioUnitario(p.getPrecio()); 
        } else {
            itemExistente = ItemCarrito.builder()
                .producto(p)
                .cantidad(request.getCantidad())
                .precioUnitario(p.getPrecio()) 
                .carrito(carrito)
                .build();
            carrito.getItems().add(itemExistente);
        }

        carritoRepo.save(carrito);
        return convertToCarritoResponse(carrito);
    }


    // Actualizar cantidad de un item en el carrito
    @Transactional
    public CarritoResponse updateItemQuantity(Long itemId, int nuevaCantidad, String emailUsuario) {
        
        // 1. Validar que la cantidad sea lógica
        if (nuevaCantidad <= 0) {
            throw new CantidadInvalidaException("La cantidad debe ser mayor a 0");
        }

        // 2. Obtener el carrito usando tu Helper y el email que viene por parámetro
        Carrito carrito = obtenerCarritoValidado(emailUsuario);

        // 3. Declarar la variable ANTES de buscarla
        ItemCarrito itemEncontrado = null;

        for (ItemCarrito it : carrito.getItems()) {
            if (it.getId().equals(itemId)) {
                itemEncontrado = it;
                break;
            }
        }

        // 4. Validar si el item realmente estaba en el carrito
        if (itemEncontrado == null) {
            throw new ItemCarritoNotFoundException(itemId);
        }

        // 5. Validar el stock (Nota: Usamos tu excepción específica de stock)
        if (!validarStock(itemEncontrado.getProducto(), nuevaCantidad)) {
            throw new StockInsuficienteException("La cantidad solicitada excede el stock disponible. Stock actual: " + itemEncontrado.getProducto().getStock());
        }

        // 6. Actualizar, guardar y retornar
        itemEncontrado.setCantidad(nuevaCantidad);
        carritoRepo.save(carrito);

        return convertToCarritoResponse(carrito);
    }

    // Vaciar carrito (Manual, sin comprar)
    @Transactional
    public void clearCart(String emailUsuario) {
        Carrito carrito = obtenerCarritoValidado(emailUsuario);
        carrito.getItems().clear();
        carritoRepo.save(carrito);
    }

    // Remover un item del carrito
    @Transactional
    public CarritoResponse removeItemFromCart(Long itemId, String emailUsuario) {
        Carrito carrito = obtenerCarritoValidado(emailUsuario);

        ItemCarrito itemAEliminar = carrito.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ItemCarritoNotFoundException(itemId));

        carrito.getItems().remove(itemAEliminar);
        carritoRepo.save(carrito);

        return convertToCarritoResponse(carrito);
    }
    
    //checkout del carrito
    @Transactional
    public void checkoutCarrito(String emailUsuario) {
        // 3. USO DEL HELPER NUEVAMENTE
        Carrito carrito = obtenerCarritoValidado(emailUsuario);

        if (carrito.getItems().isEmpty()) {
            throw new CarritoVacioException();
        }

        // validar que haya stock de todo antes de comprar
        for (ItemCarrito item : carrito.getItems()) {
            if (!validarStock(item.getProducto(), item.getCantidad())) {
                throw new StockInsuficienteException(
                    "Stock insuficiente para el producto: " + item.getProducto().getNombre() + 
                    ". Disponible: " + item.getProducto().getStock()
                );
            }
        }

        List<ItemPedidoRequest> itemsCarrito = carrito.getItems().stream()
                .map(i -> ItemPedidoRequest.builder()
                .productoId(i.getProducto().getId_prod())
                .cantidad(i.getCantidad())
                .build()).toList();
                
        PedidoRequest pedidoRequest = PedidoRequest.builder()
                .id_usuario(carrito.getUsuario().getId())
                .items(itemsCarrito)
                .build();
        
        pedidoService.crearPedido(pedidoRequest);

        carrito.getItems().clear();
        carritoRepo.save(carrito);
    }

    // Convertir Carrito a CarritoResponse
    private CarritoResponse convertToCarritoResponse(Carrito carrito) {
        return CarritoResponse.builder()
                .items(carrito.getItems().stream().map(i -> ItemCarritoResponse.builder()
                        .id(i.getId())
                        .id_producto(i.getProducto().getId_prod())
                        .nombreProducto(i.getProducto().getNombre()) 
                        .imagenUrl(i.getProducto().getImagenUrl())
                        .cantidad(i.getCantidad())
                        .precioUnitario(i.getPrecioUnitario())
                        .build()).toList())
                .total(carrito.getItems().stream()
                        .mapToDouble(i -> i.getCantidad() * i.getPrecioUnitario()).sum())
                .build();
    }

    private boolean validarStock (Producto producto, int cantidadSolicitada) {
        return producto.getStock() >= cantidadSolicitada;
    }
}