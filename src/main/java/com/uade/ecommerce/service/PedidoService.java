package com.uade.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.uade.ecommerce.model.Pedido;
import com.uade.ecommerce.repository.IPedidoRepository;


public class PedidoService {
    @Autowired
    private IPedidoRepository repo;


    public List<Pedido> getPedidos() {
        return repo.findAll();
    }

    public Pedido crearPedido(Pedido pedido) {
        repo.save(pedido);
        return pedido;
    }

    public void eliminarPedido(Long id) {
        if  (repo.existsById(id)){
            repo.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("No existe el pedido con el id: " + id);
        }
    }


}
