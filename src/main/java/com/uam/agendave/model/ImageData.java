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

    private Long id;

    private String nombre;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String imagenBase64;
}