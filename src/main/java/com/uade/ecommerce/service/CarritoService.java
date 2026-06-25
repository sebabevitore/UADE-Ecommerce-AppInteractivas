package com.uade.ecommerce.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // Obtener usuario autenticado desde el contexto de seguridad
    private Usuario getUsuarioAutenticado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));
    }

    // Obtener carrito del usuario autenticado
    @Transactional
    public CarritoResponse getCarritoByUsuario() {
        Usuario usuario = getUsuarioAutenticado();
        Carrito carrito = usuario.getCarrito();
        if (carrito == null) {
            throw new CarritoNotFoundException("Carrito no encontrado para el usuario");
        }
        return convertToCarritoResponse(carrito);
    }

    //no va a tener creacion de carrito porque se crea desde la creacion de usuario.

    @Transactional
    // logica para agregar item al carrito del usuario autenticado
    public CarritoResponse addItemToCart(ItemCarritoRequest request)  {

        // 1. VALIDAR CANTIDAD (Nueva validación)
        if (request.getCantidad() <= 0) {
            throw new CantidadInvalidaException("La cantidad a agregar debe ser mayor a 0. Se recibió: " + request.getCantidad());
        }

        //obtener usuario autenticado y su carrito
        Usuario usuario = getUsuarioAutenticado();
        Carrito carrito = usuario.getCarrito();
        if (carrito == null) {
            throw new CarritoNotFoundException("Carrito no encontrado para el usuario");
        }

        //buscar producto
        Producto p = productoRepo.findById(request.getId_producto())
                    .orElseThrow(() -> new ProductoNotFoundException(request.getId_producto()));

        //revisar si hay stock suficiente
        if (!validarStock(p, request.getCantidad())) {
            throw new CantidadInvalidaException("La cantidad a agregar no puede ser mayor al stock disponible. Stock actual: " + p.getStock() + ", cantidad a agregar: " + request.getCantidad());
        }
        // buscar si el prod ya esta en el carrito
        ItemCarrito itemExistente = null;
        List<ItemCarrito> items = carrito.getItems();
        for(ItemCarrito item : items) {
            // se los compara por ID
            if(item.getProducto().getId_prod().equals(request.getId_producto())) {
                itemExistente = item;
                break; // Salir del loop una vez encontrado
            }
        }

        if(itemExistente != null) {
            // si ya esta, se actualiza la cantidad y el precio unitario
            // primero validar si la cantidad total (actual + nueva) no supera el stock disponible
            int cantidadTotal = itemExistente.getCantidad() + request.getCantidad();
            if(!validarStock(p, cantidadTotal)) {
                throw new CantidadInvalidaException("La cantidad total del producto en el carrito debe ser mayor a 0. Cantidad actual: " + itemExistente.getCantidad() + ", cantidad a agregar: " + request.getCantidad());
            }
            itemExistente.setCantidad(itemExistente.getCantidad() + request.getCantidad());
            itemExistente.setPrecioUnitario(p.getPrecio()); // Usar precio actual del producto
        } else {
            // si no lo encuentra, se crea un nuevo item carrito
            itemExistente = ItemCarrito.builder()
                .producto(p)
                .cantidad(request.getCantidad())
                .precioUnitario(p.getPrecio())  // Usar precio actual del producto
                .carrito(carrito)
                .build();

            // se agrega el nuevo item a la lista de items del carrito
            items.add(itemExistente);
        }
        // se guarda el carrito con el nuevo item o la cantidad actualizada
        carritoRepo.save(carrito);

        // crear y retornar la respuesta
        return convertToCarritoResponse(carrito);

    }
    

    // Actualizar cantidad de un item en el carrito
    @Transactional
    public CarritoResponse updateItemQuantity(Long itemId, int nuevaCantidad) {
        ItemCarrito item = new ItemCarrito();
        item.setId(itemId);
        
        // Validar que la cantidad sea mayor a 0
        if (nuevaCantidad <= 0) {
            throw new CantidadInvalidaException("La cantidad debe ser mayor a 0");
        }

        // Buscar el item en la BD
        ItemCarrito itemEncontrado = null;
        Usuario usuario = getUsuarioAutenticado();
        Carrito carrito = usuario.getCarrito();
        if (carrito == null) {
            throw new CarritoNotFoundException("Carrito no encontrado para el usuario");
        }

        for (ItemCarrito it : carrito.getItems()) {
            if (it.getId().equals(itemId)) {
                itemEncontrado = it;
                break;
            }
        }

        if (itemEncontrado == null) {
            throw new ItemCarritoNotFoundException(itemId);
        }

        if (!validarStock(itemEncontrado.getProducto(), nuevaCantidad)) {
            throw new CantidadInvalidaException("La cantidad solicitada excede el stock disponible. Stock actual: " + itemEncontrado.getProducto().getStock());
        }

        itemEncontrado.setCantidad(nuevaCantidad);
        carritoRepo.save(carrito);

        return convertToCarritoResponse(carrito);
    }

    // Remover un item del carrito
    @Transactional
    public CarritoResponse removeItemFromCart(Long itemId) {
        Usuario usuario = getUsuarioAutenticado();
        Carrito carrito = usuario.getCarrito();
        if (carrito == null) {
            throw new CarritoNotFoundException("Carrito no encontrado para el usuario");
        }

        ItemCarrito itemAEliminar = carrito.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ItemCarritoNotFoundException(itemId));

        carrito.getItems().remove(itemAEliminar);
        carritoRepo.save(carrito);

        return convertToCarritoResponse(carrito);
    }

    // Vaciar carrito
    @Transactional
    public void clearCart() {
        Usuario usuario = getUsuarioAutenticado();
        Carrito carrito = usuario.getCarrito();
        if (carrito == null) {
            throw new CarritoNotFoundException("Carrito no encontrado para el usuario");
        }

        carrito.getItems().clear();
        carritoRepo.save(carrito);
    }

    // Checkout: convertir carrito a pedido
    @Transactional
    public void checkoutCarrito() {
        Usuario usuario = getUsuarioAutenticado();
        Carrito carrito = usuario.getCarrito();
        if (carrito == null) {
            throw new CarritoNotFoundException("Carrito no encontrado para el usuario");
        }

        if (carrito.getItems().isEmpty()) {
            throw new CarritoVacioException();
        }


        // transformar items del carrito a ItemPedidoRequest.
        List<ItemPedidoRequest> itemsCarrito = carrito.getItems().stream().map(i -> ItemPedidoRequest.builder()
                .productoId(i.getProducto().getId_prod())
                .cantidad(i.getCantidad())
                .build()).toList();
        // transformar el carrito a PedidoRequest (para que lo maneje PedidoService)
        PedidoRequest pedidoRequest = PedidoRequest.builder()
                .id_usuario(usuario.getId())
                .items(itemsCarrito)
                .build();
        
        // delegar la creacion del pedido al service de Pedido
        pedidoService.crearPedido(pedidoRequest);

        // Vaciar carrito después del checkout
        carrito.getItems().clear();
        carritoRepo.save(carrito);
    }

    // Convertir Carrito a CarritoResponse
    private CarritoResponse convertToCarritoResponse(Carrito carrito) {
        return CarritoResponse.builder()
                .items(carrito.getItems().stream().map(i -> ItemCarritoResponse.builder()
                        .id(i.getId())
                        .id_producto(i.getProducto().getId_prod())
                        .cantidad(i.getCantidad())
                        .precioUnitario(i.getPrecioUnitario())
                        .nombreProducto(i.getProducto().getNombre())
                        // .imagen(i.getProducto().getImagen())
                        .build()).toList())
                .total(carrito.getItems().stream()
                        .mapToDouble(i -> i.getCantidad() * i.getPrecioUnitario()).sum())
                .build();
    }

    private boolean validarStock (Producto producto, int cantidadSolicitada) {
        return producto.getStock() >= cantidadSolicitada;
    }
}