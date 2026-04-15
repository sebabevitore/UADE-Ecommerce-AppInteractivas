package com.uade.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecommerce.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{
    
}
