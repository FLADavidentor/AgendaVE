package com.uam.agendave.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Usuario extends Identifiable {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String contrasena; // Renombrado para coincidir con UsuarioDTO

    @Column(nullable = false, unique = true)
    private String correo;

    private String nombre;
    private String apellido;

    @OneToMany(mappedBy = "usuario")
    private List<Actividad> actividades;
}