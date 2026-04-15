package com.uade.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecommerce.model.Usuario;


public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

    // busca un usuario por email para login
    Optional<Usuario> findByEmail(String email);

    //verifica si ya existe un usuario con ese email para registro
    Boolean existsByEmail(String email);
}