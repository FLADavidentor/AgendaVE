package com.uam.agendave.dto.Registro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InscritoBodyDTO {
    @NotBlank(message = "El cif es obligatorio")
    private String cif;
    @NotEmpty(message = "Las actividades son obligatorias")
    private List<String> actividades;
}
