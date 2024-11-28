package com.uam.agendave.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.uam.agendave.model.TipoConvalidacion;
import com.uam.agendave.util.TipoConvalidacionKeyDeserializer;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class RegistroDTO {
    private UUID id;
    private boolean convalidacion;
    private boolean transporte;
    private String idEstudiante;
    private UUID idActividad;
    @JsonDeserialize(keyUsing = TipoConvalidacionKeyDeserializer.class)
    private Map<TipoConvalidacion, Integer> convalidacionesRealizadas;
    private int totalConvalidado; // Créditos totales convalidados
}


