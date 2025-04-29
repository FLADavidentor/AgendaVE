package com.uam.agendave.service;

import com.uam.agendave.dto.ActividadInscritaDTO;
import com.uam.agendave.dto.AsistenciaDTO;
import com.uam.agendave.dto.RegistroDTO;
import com.uam.agendave.model.Actividad;
import com.uam.agendave.model.TipoConvalidacion;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface RegistroService {
    public void guardarRegistro(RegistroDTO registroDTO, ActividadService actividadService);

    public List<ActividadInscritaDTO> buscarActividadesInscritasPorCif(String cif);

    Map<TipoConvalidacion, Integer> obtenerTotalCreditosPorTipo(String cif);

    void marcarAsistencia(AsistenciaDTO asistenciaDTO);
}
