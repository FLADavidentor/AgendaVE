package com.uam.agendave.dto.Actividad;

import com.uam.agendave.dto.EntidadesConfig.ZonaAsistenciaDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResumenActividadDTO {

    private ActividadDTO actividad;
    private LugarDTO lugar; // Opcional
    private ZonaAsistenciaDTO zonaAsistencia; // Opcional

    // Constructor completo
    public ResumenActividadDTO(ActividadDTO actividad, LugarDTO lugar, ZonaAsistenciaDTO zonaAsistencia) {
        this.actividad = actividad;
        this.lugar = lugar;
        this.zonaAsistencia = zonaAsistencia;
    }

}
