package com.uam.agendave.service;

import com.uam.agendave.dto.ConvalidacionDTO;
import com.uam.agendave.model.Convalidacion;
import com.uam.agendave.model.DetalleAsistencia;
import com.uam.agendave.model.EstadoAsistencia;
import com.uam.agendave.model.Registro;
import com.uam.agendave.repository.ConvalidacionRepository;
import com.uam.agendave.repository.DetalleAsistenciaRepository;
import com.uam.agendave.service.ConvalidacionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ConvalidacionServiceImpl implements ConvalidacionService {

    private final ConvalidacionRepository convalidacionRepository;
    private final DetalleAsistenciaRepository detalleAsistenciaRepository;

    public ConvalidacionServiceImpl(ConvalidacionRepository convalidacionRepository,
                                    DetalleAsistenciaRepository detalleAsistenciaRepository) {
        this.convalidacionRepository = convalidacionRepository;
        this.detalleAsistenciaRepository = detalleAsistenciaRepository;
    }

    @Override
    public void convalidarCreditos(UUID idDetalleAsistencia) {
        DetalleAsistencia detalle = detalleAsistenciaRepository.findById(idDetalleAsistencia)
                .orElseThrow(() -> new IllegalArgumentException("DetalleAsistencia no encontrado"));

        Registro registro = detalle.getRegistro();
        if (registro == null) {
            throw new IllegalStateException("El DetalleAsistencia no está asociado a un registro válido.");
        }

        // Validar reglas de negocio
        if (detalle.getEstadoAsistencia() == EstadoAsistencia.PRESENTE
                && registro.isConvalidacion()
                && registro.getActividad().getConvalidacionesPermitidas() != null) {

            int usados = convalidacionRepository.findByActividadId(registro.getActividad().getId())
                    .stream()
                    .mapToInt(Convalidacion::getCreditosConvalidados)
                    .sum();

            if (usados < registro.getActividad().getTotalConvalidacionesPermitidas()) {
                Convalidacion nuevaConvalidacion = new Convalidacion();
                nuevaConvalidacion.setCif(registro.getCif()); // Usa el CIF directamente
                nuevaConvalidacion.setActividad(registro.getActividad());
                nuevaConvalidacion.setCreditosConvalidados(1);

                convalidacionRepository.save(nuevaConvalidacion);
            }
        }
    }

    @Override
    public List<ConvalidacionDTO> obtenerPorEstudiante(String idEstudiante) {
        return convalidacionRepository.findByCif(idEstudiante)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConvalidacionDTO> obtenerPorActividad(UUID idActividad) {
        return convalidacionRepository.findByActividadId(idActividad)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Conversión de entidad a DTO
    private ConvalidacionDTO convertirADTO(Convalidacion convalidacion) {
        ConvalidacionDTO dto = new ConvalidacionDTO();
        dto.setId(convalidacion.getId());
        dto.setIdEstudiante(convalidacion.getCif());
        dto.setIdActividad(convalidacion.getActividad().getId());
        dto.setCreditosConvalidados(convalidacion.getCreditosConvalidados());
        return dto;
    }
}
