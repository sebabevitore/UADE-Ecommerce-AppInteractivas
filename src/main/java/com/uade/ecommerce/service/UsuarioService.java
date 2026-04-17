package com.uade.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecommerce.dto.request.UsuarioRegisterDTO;
import com.uade.ecommerce.exception.UsuarioNotFoundException;
import com.uade.ecommerce.model.Usuario;
import com.uade.ecommerce.repository.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repo;

//    public List<Usuario> getUsuarios() {
//        return repo.findAll();
//    }    

    public List<UsuarioRegisterDTO> getUsuarios() {
        return repo.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

//    public Usuario crearUsuario(Usuario usuario) {
//        repo.save(usuario);
//        return usuario;
//    }    

    public UsuarioRegisterDTO crearUsuario(UsuarioRegisterDTO dto) {
        // 1. Pasamos del DTO a la Entidad (lo que se guarda en MySQL)
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setSexo(dto.getSexo());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        
        // 2. Guardamos en la base de datos
        Usuario guardado = repo.save(usuario);
        
        // 3. Devolvemos el DTO
        return convertToDTO(guardado);
    }

    public void eliminarUsuario(Long id) {
        if (!repo.existsById(id)) {
            throw new UsuarioNotFoundException("No existe el usuario con el id: " + id);
        }
        repo.deleteById(id);
    }

    private UsuarioRegisterDTO convertToDTO(Usuario usuario) {
        return UsuarioRegisterDTO.builder()
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .sexo(usuario.getSexo())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .build();
    }

}
