package com.uam.agendave.service;

import com.uam.agendave.dto.RegistroDTO;
import com.uam.agendave.model.Actividad;
import com.uam.agendave.model.Registro;
import com.uam.agendave.model.TipoConvalidacion;
import com.uam.agendave.repository.ActividadRepository;
import com.uam.agendave.repository.RegistroRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RegistroServiceImpl implements RegistroService {

    private final RegistroRepository registroRepository;
    private final ActividadRepository actividadRepository;

    public RegistroServiceImpl(RegistroRepository registroRepository, ActividadRepository actividadRepository) {
        this.registroRepository = registroRepository;
        this.actividadRepository = actividadRepository;
    }

    @Override
    public List<RegistroDTO> obtenerTodos() {
        return registroRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public RegistroDTO guardarRegistro(RegistroDTO registroDTO) {
        // Convertir el DTO a entidad
        Registro registro = convertirAEntidad(registroDTO);

        // Validar las convalidaciones realizadas
        if (registroDTO.isConvalidacion() && registroDTO.getConvalidacionesRealizadas() != null) {
            Actividad actividad = actividadRepository.findById(registroDTO.getIdActividad())
                    .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada con ID: " + registroDTO.getIdActividad()));
            validarConvalidaciones(registroDTO, actividad);
            int totalConvalidado = registroDTO.getConvalidacionesRealizadas().values().stream().mapToInt(Integer::intValue).sum();
            registro.setTotalConvalidado(totalConvalidado);
        }

        Registro nuevoRegistro = registroRepository.save(registro);
        return convertirADTO(nuevoRegistro);
    }

    @Override
    public RegistroDTO buscarPorId(UUID id) {
        Registro registro = registroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro no encontrado con ID: " + id));
        return convertirADTO(registro);
    }

    @Override
    public void eliminarRegistro(UUID id) {
        if (!registroRepository.existsById(id)) {
            throw new IllegalArgumentException("Registro no encontrado con ID: " + id);
        }
        registroRepository.deleteById(id);
    }

    @Override
    public List<RegistroDTO> buscarPorEstudiante(String cif) {
        return registroRepository.findByCif(cif)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegistroDTO> buscarPorActividad(UUID idActividad) {
        return registroRepository.findByActividadId(idActividad)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public Registro convertirADetalleAsistencia(RegistroDTO registroDTO) {
        Registro registro = new Registro();
        registro.setId(registroDTO.getId());
        registro.setConvalidacion(registroDTO.isConvalidacion());
        registro.setTransporte(registroDTO.isTransporte());

        if (registroDTO.getIdEstudiante() != null) {
            registro.setCif(registroDTO.getIdEstudiante()); // Asigna directamente el CIF
        }

        if (registroDTO.getIdActividad() != null) {
            Actividad actividad = new Actividad();
            actividad.setId(registroDTO.getIdActividad());
            registro.setActividad(actividad);
        }

        if (registroDTO.isConvalidacion() && registroDTO.getConvalidacionesRealizadas() != null) {
            registro.setConvalidacionesRealizadas(new HashMap<>(registroDTO.getConvalidacionesRealizadas()));
        }

        return registro;
    }


    private Registro convertirAEntidad(RegistroDTO registroDTO) {
        Registro registro = new Registro();
        registro.setId(registroDTO.getId());
        registro.setConvalidacion(registroDTO.isConvalidacion());
        registro.setTransporte(registroDTO.isTransporte());

        if (registroDTO.getIdEstudiante() != null) {
            registro.setCif(registroDTO.getIdEstudiante()); // Asigna directamente el CIF
        }

        if (registroDTO.getIdActividad() != null) {
            Actividad actividad = actividadRepository.findById(registroDTO.getIdActividad())
                    .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada con ID: " + registroDTO.getIdActividad()));
            registro.setActividad(actividad);
        }

        if (registroDTO.isConvalidacion() && registroDTO.getConvalidacionesRealizadas() != null) {
            registro.setConvalidacionesRealizadas(new HashMap<>(registroDTO.getConvalidacionesRealizadas()));
        }

        return registro;
    }

    /**
     * Valida las convalidaciones realizadas en base a la actividad.
     */
    private void validarConvalidaciones(RegistroDTO registroDTO, Actividad actividad) {
        Map<TipoConvalidacion, Integer> limitesConvalidacion = actividad.getConvalidacionesPermitidas();
        Integer limiteTotal = actividad.getTotalConvalidacionesPermitidas();

        if (limitesConvalidacion == null || limitesConvalidacion.isEmpty() || limiteTotal == null || limiteTotal <= 0) {
            throw new IllegalStateException("La actividad no permite convalidaciones.");
        }

        // Validar límites por tipo
        registroDTO.getConvalidacionesRealizadas().forEach((tipo, cantidad) -> {
            int limiteTipo = limitesConvalidacion.getOrDefault(tipo, 0);
            if (cantidad > limiteTipo) {
                throw new IllegalStateException("Excedido el límite para el tipo de convalidación: " + tipo);
            }
        });

        // Validar límite total
        int totalRealizado = registroDTO.getConvalidacionesRealizadas().values().stream().mapToInt(Integer::intValue).sum();
        if (totalRealizado > limiteTotal) {
            throw new IllegalStateException("Excedido el límite total de convalidaciones permitidas para esta actividad.");
        }

        // Actualizar el total en el DTO para reflejar el cálculo
        registroDTO.setTotalConvalidado(totalRealizado);
    }

    private RegistroDTO convertirADTO(Registro registro) {
        RegistroDTO registroDTO = new RegistroDTO();
        registroDTO.setId(registro.getId());
        registroDTO.setConvalidacion(registro.isConvalidacion());
        registroDTO.setTransporte(registro.isTransporte());
        registroDTO.setIdEstudiante(registro.getCif());
        registroDTO.setIdActividad(registro.getActividad().getId());
        registroDTO.setTotalConvalidado(registro.getTotalConvalidado());

        if (registro.isConvalidacion() && registro.getConvalidacionesRealizadas() != null) {
            registroDTO.setConvalidacionesRealizadas(new HashMap<>(registro.getConvalidacionesRealizadas()));
        }

        return registroDTO;
    }
}
