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

import com.uade.ecommerce.dto.request.PedidoRequest;
import com.uade.ecommerce.dto.response.PedidoResponse;
import com.uade.ecommerce.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    @Autowired
    private PedidoService pedidoServ;

//    @GetMapping()
//    public List<Pedido> getPedido() {
//        return pedidoServ.getPedidos();
//    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> getPedidos() {
        List<PedidoResponse> lista = pedidoServ.getPedidos();
        return ResponseEntity.ok(lista); // Retorna 200 OK
    }

//    @PostMapping
//    public Pedido addPedido(@RequestBody Pedido pedido) {
//        return pedidoServ.crearPedido(pedido);
//    }

    @PostMapping
    public ResponseEntity<PedidoResponse> addPedido(@RequestBody PedidoRequest pedidoRequest) {
        PedidoResponse nuevo = pedidoServ.crearPedido(pedidoRequest);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED); // 201 Created
    }

//    @DeleteMapping("/{id}")
//    public void eliminarPedido(@PathVariable Long id) {
//        pedidoServ.eliminarPedido(id);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        pedidoServ.eliminarPedido(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
