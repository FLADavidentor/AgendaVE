package com.uam.agendave.dto.Actividad;

import com.uam.agendave.model.Actividad;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActividadInscritaDTO {
    private Actividad actividad;
    private String tipoConvalidacion;
}
