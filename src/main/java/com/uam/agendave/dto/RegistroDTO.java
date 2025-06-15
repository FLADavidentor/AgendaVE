package com.uam.agendave.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.uam.agendave.model.TipoConvalidacion;
import com.uam.agendave.util.TipoConvalidacionKeyDeserializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class RegistroDTO {

    @NotBlank(message = "El CIF del estudiante es obligatorio")
    private String cif;
    @NotNull(message = "El id de Actividad es obligatorio")
    private UUID idActividad;
    @NotNull(message = "El tipo de convalidacion es obligatorio")
    private TipoConvalidacion tipoConvalidacion;
    private Boolean transporte;


}


