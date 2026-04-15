package com.uade.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecommerce.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
    
}
