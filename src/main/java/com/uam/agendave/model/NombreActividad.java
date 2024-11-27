package com.uam.agendave.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NombreActividad extends Identifiable {
    @Column(nullable = false, unique = true)
    private String nombre;
}

