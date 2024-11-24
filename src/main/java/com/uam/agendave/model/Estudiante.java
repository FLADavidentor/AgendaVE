package com.uam.agendave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Estudiante extends Identifiable{
    private String nombre;
    private String nombre2;
    private String apellido;
    private String apellido2;
    private String correo;
    private String numeroTelefono;
    private String cif;

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Registro> registros;
}
