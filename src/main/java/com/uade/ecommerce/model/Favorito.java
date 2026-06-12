package com.uade.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación bidireccional con Usuario (completa el mappedBy = "favorito" que ya tienes en Usuario.java)
    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "favoritos_productos", // Nombre de la tabla intermedia en la BD
        joinColumns = @JoinColumn(name = "favorito_id"), // Clave foránea de esta entidad (Favorito)
        inverseJoinColumns = @JoinColumn(name = "producto_id") // Clave foránea de la otra entidad (Producto)
    )
    private List<Producto> productos = new ArrayList<>();
}