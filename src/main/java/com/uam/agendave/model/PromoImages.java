package com.uam.agendave.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PromoImages extends Identifiable{
    @Column(name = "imagen_path")
    private String imagenPath;
}
