package com.uade.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uade.ecommerce.model.Favorito;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
}