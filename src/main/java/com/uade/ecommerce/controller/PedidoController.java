package com.uade.ecommerce.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.uade.ecommerce.model.Pedido;
import com.uade.ecommerce.service.PedidoService;

public class PedidoController {
    private PedidoService pedidoServ;

    @GetMapping()
    public List<Pedido> getPedido() {
        return pedidoServ.getPedidos();
    }

    @PostMapping
    public Pedido addPedido(@RequestBody Pedido pedido) {
        return pedidoServ.crearPedido(pedido);
    }

    @DeleteMapping("/{id}")
    public void eliminarPedido(@PathVariable Long id) {
        pedidoServ.eliminarPedido(id);
    }


}
