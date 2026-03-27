package com.uade.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecommerce.model.Usuario;
import com.uade.ecommerce.repository.IUsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private IUsuarioRepository repo;


    public List<Usuario> getUsuarios() {
        return repo.findAll();
    }

    public Usuario crearUsuario(Usuario usuario) {
        repo.save(usuario);
        return usuario;
    }

    public void eliminarUsuario(Long id) {
        if  (repo.existsById(id)){
            repo.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("No existe el usuario con el id: " + id);
        }
    }


}
