package com.uade.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.uade.ecommerce.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
        Optional<Usuario> findByEmail(String email);

        Boolean existsByEmail(String email);

}
