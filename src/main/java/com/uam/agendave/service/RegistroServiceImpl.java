package com.uam.agendave.service;

import com.uam.agendave.dto.RegistroDTO;
import com.uam.agendave.model.Actividad;
import com.uam.agendave.model.Estudiante;
import com.uam.agendave.model.Registro;
import com.uam.agendave.model.TipoConvalidacion;
import com.uam.agendave.repository.ActividadRepository;
import com.uam.agendave.repository.EstudianteRepository;
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
            // Obtener la actividad asociada al registro
            Actividad actividad = actividadRepository.findById(registroDTO.getIdActividad())
                    .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada con ID: " + registroDTO.getIdActividad()));

            // Validar las convalidaciones contra los límites
            validarConvalidaciones(registroDTO, actividad);

            // Calcular el total de convalidaciones realizadas
            int totalConvalidado = registroDTO.getConvalidacionesRealizadas().values().stream().mapToInt(Integer::intValue).sum();
            registro.setTotalConvalidado(totalConvalidado);
        }

        // Guardar el registro en la base de datos
        Registro nuevoRegistro = registroRepository.save(registro);

        // Retornar el DTO generado
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
    public List<RegistroDTO> buscarPorEstudiante(UUID idEstudiante) {
        return registroRepository.findByEstudianteId(idEstudiante)
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

    private Registro convertirAEntidad(RegistroDTO registroDTO) {
        Registro registro = new Registro();
        registro.setId(registroDTO.getId());
        registro.setConvalidacion(registroDTO.isConvalidacion());
        registro.setTransporte(registroDTO.isTransporte());

        if (registroDTO.getIdEstudiante() != null) {
            Estudiante estudiante = new Estudiante();
            estudiante.setId(registroDTO.getIdEstudiante());
            registro.setEstudiante(estudiante);
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
        registroDTO.setIdEstudiante(registro.getEstudiante().getId());
        registroDTO.setIdActividad(registro.getActividad().getId());
        registroDTO.setTotalConvalidado(registro.getTotalConvalidado());

        if (registro.isConvalidacion() && registro.getConvalidacionesRealizadas() != null) {
            registroDTO.setConvalidacionesRealizadas(new HashMap<>(registro.getConvalidacionesRealizadas()));
        }

        return registroDTO;
    }

    @Override
    public Registro convertirADetalleAsistencia(RegistroDTO registroDTO) {
        Registro registro = new Registro();
        registro.setId(registroDTO.getId());
        registro.setConvalidacion(registroDTO.isConvalidacion());
        registro.setTransporte(registroDTO.isTransporte());

        if (registroDTO.getIdEstudiante() != null) {
            Estudiante estudiante = new Estudiante();
            estudiante.setId(registroDTO.getIdEstudiante());
            registro.setEstudiante(estudiante);
        }

        if (registroDTO.getIdActividad() != null) {
            Actividad actividad = new Actividad();
            actividad.setId(registroDTO.getIdActividad());
            registro.setActividad(actividad);
        }

        return registro;
    }

}

