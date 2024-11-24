package com.uam.agendave.service;

import com.uam.agendave.dto.ActividadDTO;
import com.uam.agendave.model.*;
import com.uam.agendave.repository.ActividadRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ActividadServiceImpl implements ActividadService {

    private final ActividadRepository actividadRepository;

    public ActividadServiceImpl(ActividadRepository actividadRepository) {
        this.actividadRepository = actividadRepository;
    }

    @Override
    public List<ActividadDTO> obtenerTodas() {
        List<Actividad> actividades = actividadRepository.findAll();
        return actividades.stream()
                .map(this::convertirAModelDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ActividadDTO guardarActividad(ActividadDTO actividadDTO) {
        Actividad actividad = convertirAEntidad(actividadDTO);
        Actividad actividadGuardada = actividadRepository.save(actividad);
        return convertirAModelDTO(actividadGuardada);
    }

    @Override
    public Actividad buscarPorId(UUID id) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada con ID: " + id));
        return actividad;  // Asegúrate de convertir de Actividad a ActividadDTO
    }

    @Override
    public ActividadDTO actualizarActividad(ActividadDTO actividadDTO) {
        return null;
    }

    @Override
    public void eliminarActividad(UUID id) {
        if (!actividadRepository.existsById(id)) {
            throw new IllegalArgumentException("Actividad no encontrada con ID: " + id);
        }
        actividadRepository.deleteById(id);
    }

    @Override
    public List<ActividadDTO> buscarPorNombre(String nombre) {
        List<Actividad> actividades = actividadRepository.findByNombreActividadNombre(nombre);
        return actividades.stream()
                .map(this::convertirAModelDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ActividadDTO> buscarPorLugar(UUID idLugar) {
        List<Actividad> actividades = actividadRepository.findByLugarId(idLugar);
        return actividades.stream()
                .map(this::convertirAModelDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ActividadDTO> buscarActividadesConCupoDisponible() {
        List<Actividad> actividades = actividadRepository.findActividadesConCupoDisponible();
        return actividades.stream()
                .map(this::convertirAModelDTO)
                .collect(Collectors.toList());
    }

    // Métodos de conversión entre entidad y DTO
    private ActividadDTO convertirAModelDTO(Actividad actividad) {
        ActividadDTO actividadDTO = new ActividadDTO();
        actividadDTO.setId(actividad.getId());
        actividadDTO.setDescripcion(actividad.getDescripcion());
        actividadDTO.setFecha(actividad.getFecha());
        actividadDTO.setHoraInicio(actividad.getHoraInicio());
        actividadDTO.setHoraFin(actividad.getHoraFin());
        actividadDTO.setEstado(actividad.isEstado());
        actividadDTO.setCupo(actividad.getCupo());

        // Relación con NombreActividad
        actividadDTO.setIdNombreActividad(actividad.getNombreActividad() != null ? actividad.getNombreActividad().getId() : null);

        // Relación con Lugar
        actividadDTO.setIdLugar(actividad.getLugar() != null ? actividad.getLugar().getId() : null);

        // Relación con TipoActividad
        actividadDTO.setIdTipoActividad(actividad.getTipoActividad() != null ? actividad.getTipoActividad().getId() : null);

        // Relación con Usuario
        actividadDTO.setIdUsuario(actividad.getUsuario() != null ? actividad.getUsuario().getId() : null);

        // Aquí se debe agregar la lógica para mapear las convalidaciones
        Map<TipoConvalidacion, Integer> convalidacionesPermitidas = actividad.getConvalidacionesPermitidas();
        actividadDTO.setConvalidacionesPermitidas(convalidacionesPermitidas);
        actividadDTO.setTotalConvalidacionesPermitidas(actividad.getTotalConvalidacionesPermitidas());

        return actividadDTO;
    }

    private Actividad convertirAEntidad(ActividadDTO actividadDTO) {
        Actividad actividad = new Actividad();
        actividad.setId(actividadDTO.getId());
        actividad.setDescripcion(actividadDTO.getDescripcion());
        actividad.setFecha(actividadDTO.getFecha());
        actividad.setHoraInicio(actividadDTO.getHoraInicio());
        actividad.setHoraFin(actividadDTO.getHoraFin());
        actividad.setEstado(actividadDTO.isEstado());
        actividad.setCupo(actividadDTO.getCupo());

        // Relación con NombreActividad
        if (actividadDTO.getIdNombreActividad() != null) {
            actividad.setNombreActividad(new NombreActividad());
            actividad.getNombreActividad().setId(actividadDTO.getIdNombreActividad());
        }

        // Relación con Lugar
        if (actividadDTO.getIdLugar() != null) {
            actividad.setLugar(new Lugar());
            actividad.getLugar().setId(actividadDTO.getIdLugar());
        }

        // Relación con TipoActividad
        if (actividadDTO.getIdTipoActividad() != null) {
            actividad.setTipoActividad(new TipoActividad());
            actividad.getTipoActividad().setId(actividadDTO.getIdTipoActividad());
        }

        // Relación con Usuario
        if (actividadDTO.getIdUsuario() != null) {
            actividad.setUsuario(new Usuario());
            actividad.getUsuario().setId(actividadDTO.getIdUsuario());
        }

        // Aquí se debe agregar la lógica para mapear las convalidaciones
        actividad.setConvalidacionesPermitidas(actividadDTO.getConvalidacionesPermitidas());
        actividad.setTotalConvalidacionesPermitidas(actividadDTO.getTotalConvalidacionesPermitidas());

        return actividad;
    }
}