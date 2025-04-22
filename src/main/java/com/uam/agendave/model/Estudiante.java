package com.uam.agendave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Estudiante extends Identifiable {
    private String cif;
    private String password;
    private String nombres;
    private String apellidos;
    private String tipo;
    private String correo;
    private String sexo;
    private String telefono;
    private String carrera;
    private String facultad;
}

