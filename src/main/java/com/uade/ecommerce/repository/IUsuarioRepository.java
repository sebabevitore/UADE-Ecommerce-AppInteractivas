package com.uade.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecommerce.model.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long>{
    
}
