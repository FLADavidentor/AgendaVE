package com.uam.agendave.service.Registro;

import com.uam.agendave.dto.ActividadInscritaDTO;
import com.uam.agendave.dto.AsistenciaDTO;
import com.uam.agendave.dto.RegistroDTO;
import com.uam.agendave.model.TipoConvalidacion;
import com.uam.agendave.service.actividad.ActividadService;

import java.util.List;
import java.util.Map;

public interface RegistroService {
    public void guardarRegistro(RegistroDTO registroDTO, ActividadService actividadService);

    public List<ActividadInscritaDTO> buscarActividadesInscritasPorCif(String cif);

    Map<TipoConvalidacion, Integer> obtenerTotalCreditosPorTipo(String cif);

    void marcarAsistencia(AsistenciaDTO asistenciaDTO);
}
