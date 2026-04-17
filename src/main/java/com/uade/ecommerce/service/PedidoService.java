package com.uade.ecommerce.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.uade.ecommerce.dto.request.ItemPedidoRequest;
import com.uade.ecommerce.dto.request.PedidoRequest;
import com.uade.ecommerce.dto.response.ItemPedidoResponse;
import com.uade.ecommerce.dto.response.PedidoResponse;
import com.uade.ecommerce.model.LineaPedido;
import com.uade.ecommerce.model.Pedido;
import com.uade.ecommerce.model.Producto;
import com.uade.ecommerce.model.Usuario;
import com.uade.ecommerce.repository.PedidoRepository;
import com.uade.ecommerce.repository.ProductoRepository;
import com.uade.ecommerce.repository.UsuarioRepository;

@Service
public class PedidoService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private PedidoRepository repo;

    @Autowired
    private ProductoRepository productoRepo;


//    public List<Pedido> getPedidos() {
//        return repo.findAll();
//    }

    public List<PedidoResponse> getPedidos() {
        return repo.findAll().stream()
                .map(this::convertToPedidoDTO) // Transformamos cada Pedido en PedidoResponse
                .toList();
    }


//    public Pedido crearPedido(Pedido pedido) {
//        repo.save(pedido);
//        return pedido;
//    }    

    public PedidoResponse crearPedido(PedidoRequest request) {
        // 1. Obtenemos el email del usuario logueado desde el contexto de Seguridad
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
    
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


        // 2. Creamos el objeto Pedido principal
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFecha(LocalDate.now());
    
        // 3. Transformamos cada item del Request en una LineaPedido
        List<LineaPedido> lineas = new ArrayList<>();
        double totalPedido = 0;

        for (ItemPedidoRequest itemReq : request.getItems()) {
            Producto p = productoRepo.findById(itemReq.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + itemReq.getProductoId()));

            LineaPedido linea = new LineaPedido();
            linea.setProducto(p);
            linea.setCantidad(itemReq.getCantidad());
            linea.setPrecioUnitario(p.getPrecio()); // Congelamos el precio actual
            linea.setPedido(pedido); // Muy importante para vincular la línea al pedido

            lineas.add(linea);
            totalPedido += (p.getPrecio() * itemReq.getCantidad());
        }

        // 4. Guardamos todo (CascadeType.ALL en Pedido guardará las líneas automáticamente)
        pedido.setLineas(lineas);
        Pedido pedidoGuardado = repo.save(pedido);

        // 5. Mapeamos a la respuesta que definimos antes
        List<ItemPedidoResponse> detalles = pedidoGuardado.getLineas().stream().map(l -> 
            ItemPedidoResponse.builder()
                .nombreProducto(l.getProducto().getNombre())
                .cantidad(l.getCantidad())
                .precioUnitario(l.getPrecioUnitario())
                .build()
        ).toList();

        return PedidoResponse.builder()
                .id(pedidoGuardado.getId())
                .fecha(pedidoGuardado.getFecha().atStartOfDay())
                .total(totalPedido)
                .estado("PENDIENTE")
                .detalles(detalles)
                .build();
    }


    public void eliminarPedido(Long id) {
        if  (repo.existsById(id)){
            repo.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("No existe el pedido con el id: " + id);
        }
    }

    private PedidoResponse convertToPedidoDTO(Pedido pedido) {
    return PedidoResponse.builder()
            .id(pedido.getId())
            .fecha(pedido.getFecha().atStartOfDay())
            .total(pedido.getLineas().stream().mapToDouble(l -> l.getPrecioUnitario() * l.getCantidad()).sum())
            .estado("PENDIENTE") // O el campo que tengas en la entidad
            .detalles(pedido.getLineas().stream().map(l -> 
                ItemPedidoResponse.builder()
                    .nombreProducto(l.getProducto().getNombre())
                    .cantidad(l.getCantidad())
                    .precioUnitario(l.getPrecioUnitario())
                    .build()
            ).toList())
            .build();
}


}
