package com.uam.agendave.dto.Actividad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TransporteDTO {
    private UUID id;
    @NotBlank(message = "Debe incluirse el Lugar de Transporte")
    private String lugar;
    @NotNull(message = "Debe incluirse la capacidad de Transporte")
    private Integer capacidad;
}
