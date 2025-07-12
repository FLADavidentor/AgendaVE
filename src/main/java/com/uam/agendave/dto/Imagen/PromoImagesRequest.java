package com.uam.agendave.dto.Imagen;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromoImagesRequest {
    @NotBlank(message = "La Imagen es requerida")
    private String ImagenBase64;
}
