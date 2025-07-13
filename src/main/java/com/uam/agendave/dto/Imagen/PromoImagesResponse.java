package com.uam.agendave.dto.Imagen;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromoImagesResponse {
    @NotBlank(message = "El nombre de la imagen es requerido")
    private String imagenPath;
}
