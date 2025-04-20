package com.uam.agendave.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Table(name = "imageData")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageData extends Identifiable {



    private String nombre;

    @Lob
    @Column(name = "imagen_base64", columnDefinition = "TEXT")
    private String imagenBase64;
}