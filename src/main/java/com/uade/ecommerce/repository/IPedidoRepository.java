package com.uade.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecommerce.model.Pedido;

public interface IPedidoRepository extends JpaRepository<Pedido, Long>{
    
}
