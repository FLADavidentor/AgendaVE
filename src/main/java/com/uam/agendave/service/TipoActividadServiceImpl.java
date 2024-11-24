package com.uam.agendave.service;

import com.uam.agendave.dto.TipoActividadDTO;
import com.uam.agendave.model.TipoActividad;
import com.uam.agendave.repository.TipoActividadRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TipoActividadServiceImpl implements TipoActividadService {

    private final TipoActividadRepository tipoActividadRepository;

    public TipoActividadServiceImpl(TipoActividadRepository tipoActividadRepository) {
        this.tipoActividadRepository = tipoActividadRepository;
    }

    @Override
    public List<TipoActividadDTO> obtenerTodos() {
        return tipoActividadRepository.findAll()
                .stream()
                .map(this::convertirAModeloDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TipoActividadDTO guardarTipoActividad(TipoActividadDTO tipoActividadDTO) {
        TipoActividad tipoActividad = convertirADominio(tipoActividadDTO);
        tipoActividad = tipoActividadRepository.save(tipoActividad);
        return convertirAModeloDTO(tipoActividad);
    }

    @Override
    public TipoActividadDTO buscarPorId(UUID id) {
        TipoActividad tipoActividad = tipoActividadRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Tipo de actividad no encontrado con ID: " + id));
        return convertirAModeloDTO(tipoActividad);
    }

    @Override
    public void eliminarTipoActividad(UUID id) {
        if (!tipoActividadRepository.existsById(id)) {
            throw new IllegalArgumentException("Tipo de actividad no encontrado con ID: " + id);
        }
        tipoActividadRepository.deleteById(id);
    }

    @Override
    public List<TipoActividadDTO> buscarPorNombre(String nombreTipo) {
        return tipoActividadRepository.findByNombreTipoContainingIgnoreCase(nombreTipo)
                .stream()
                .map(this::convertirAModeloDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TipoActividadDTO> buscarPorFacultadEncargada(String facultad) {
        return tipoActividadRepository.findByFacultadEncargadaContainingIgnoreCase(facultad)
                .stream()
                .map(this::convertirAModeloDTO)
                .collect(Collectors.toList());
    }

    // Método para convertir el modelo de entidad TipoActividad a TipoActividadDTO
    private TipoActividadDTO convertirAModeloDTO(TipoActividad tipoActividad) {
        TipoActividadDTO dto = new TipoActividadDTO();
        dto.setId(tipoActividad.getId());
        dto.setNombreTipo(tipoActividad.getNombreTipo());
        dto.setFacultadEncargada(tipoActividad.getFacultadEncargada());
        return dto;
    }

    // Método para convertir TipoActividadDTO a TipoActividad
    private TipoActividad convertirADominio(TipoActividadDTO tipoActividadDTO) {
        TipoActividad tipoActividad = new TipoActividad();
        tipoActividad.setId(tipoActividadDTO.getId());
        tipoActividad.setNombreTipo(tipoActividadDTO.getNombreTipo());
        tipoActividad.setFacultadEncargada(tipoActividadDTO.getFacultadEncargada());
        return tipoActividad;
    }
}
