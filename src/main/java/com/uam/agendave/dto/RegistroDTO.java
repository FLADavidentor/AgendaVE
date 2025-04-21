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

    private String cif;
    private UUID idActividad;
    private Boolean transporte;
    private TipoConvalidacion tipoConvalidacion;

}


