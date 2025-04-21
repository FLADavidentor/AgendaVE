package com.uam.agendave.service;

import com.uam.agendave.dto.RegistroDTO;

public interface RegistroService {
    public void guardarRegistro(RegistroDTO registroDTO, ActividadService actividadService);
}
