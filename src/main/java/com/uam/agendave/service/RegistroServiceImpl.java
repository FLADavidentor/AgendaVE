package com.uam.agendave.service;

import com.uam.agendave.dto.RegistroDTO;
import com.uam.agendave.model.Actividad;
import com.uam.agendave.model.Estudiante;
import com.uam.agendave.model.Registro;
import com.uam.agendave.repository.RegistroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RegistroServiceImpl implements RegistroService {

    private final RegistroRepository registroRepository;

    public RegistroServiceImpl(RegistroRepository registroRepository) {
        this.registroRepository = registroRepository;
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
        Registro registro = convertirAEntidad(registroDTO);
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
            Actividad actividad = new Actividad();
            actividad.setId(registroDTO.getIdActividad());
            registro.setActividad(actividad);
        }

        return registro;
    }

    private RegistroDTO convertirADTO(Registro registro) {
        RegistroDTO registroDTO = new RegistroDTO();
        registroDTO.setId(registro.getId());
        registroDTO.setConvalidacion(registro.isConvalidacion());
        registroDTO.setTransporte(registro.isTransporte());

        if (registro.getEstudiante() != null) {
            registroDTO.setIdEstudiante(registro.getEstudiante().getId());
        }

        if (registro.getActividad() != null) {
            registroDTO.setIdActividad(registro.getActividad().getId());
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

