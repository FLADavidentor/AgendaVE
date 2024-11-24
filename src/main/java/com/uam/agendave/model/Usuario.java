package com.uam.agendave.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Usuario extends Identifiable {

    private String username;
    private String password;
    private String correo;
    private String nombre;
    private String apellido;

    @OneToMany(mappedBy = "usuario")
    private List<Actividad> actividades;
}