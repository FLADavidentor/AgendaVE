package com.uam.agendave.service.actividad;

import com.uam.agendave.dto.Actividad.ActividadDTO;

public interface ActividadNotifService {

    void notificarActividadPublicada(ActividadDTO actividadDTO);

    void notificarActividadDesPublicada(ActividadDTO actividadDTO);
}
