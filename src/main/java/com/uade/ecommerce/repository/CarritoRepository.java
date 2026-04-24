package com.uade.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecommerce.model.Carrito;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
}
