package com.uam.agendave.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Transporte extends Identifiable{
    @ManyToOne
    @JoinColumn(name = "idLugar")
    public Lugar lugar;
    public Integer capacidad;
}
