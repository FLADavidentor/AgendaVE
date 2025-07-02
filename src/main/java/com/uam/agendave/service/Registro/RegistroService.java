package com.uam.agendave.service.Registro;

import com.uam.agendave.dto.Actividad.ActividadInscritaDTO;
import com.uam.agendave.dto.Actividad.LugarDTO;
import com.uam.agendave.dto.Registro.*;
import com.uam.agendave.model.TipoConvalidacion;
import com.uam.agendave.service.actividad.ActividadService;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface RegistroService {
    public void guardarRegistro(RegistroDTO registroDTO, ActividadService actividadService);

    public List<ActividadInscritaDTO> buscarActividadesInscritasPorCif(String cif);

    Map<TipoConvalidacion, Integer> obtenerTotalCreditosPorTipo(String cif);

    void marcarAsistencia(AsistenciaDTO asistenciaDTO);

    List<InscritoResponseDTO> verificarValorDeInscripcion(InscritoBodyDTO inscritoBodyDTO);

    public ResponseEntity<?> calcularAsistencia(LocationAsistenciaDTO dto);
}
