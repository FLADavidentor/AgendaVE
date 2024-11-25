package com.uam.agendave.service;

import com.uam.agendave.dto.EstudianteDTO;
import com.uam.agendave.model.Estudiante;
import com.uam.agendave.repository.EstudianteRepository;
import com.uam.agendave.service.EstudianteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;

    public EstudianteServiceImpl(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    @Override
    public List<EstudianteDTO> obtenerTodos() {
        return estudianteRepository.findAll().stream()
                .map(this::convertirAModeloDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EstudianteDTO guardarEstudiante(EstudianteDTO estudianteDTO) {
        Estudiante estudiante = convertirAEntidad(estudianteDTO);
        Estudiante estudianteGuardado = estudianteRepository.save(estudiante);
        return convertirAModeloDTO(estudianteGuardado);
    }

    @Override
    public EstudianteDTO buscarPorId(UUID id) {
        Estudiante estudiante = estudianteRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Estudiante no encontrado con ID: " + id));
        return convertirAModeloDTO(estudiante);
    }

    @Override
    public List<EstudianteDTO> buscarPorNombreYApellido(String nombre, String apellido) {
        return estudianteRepository.findByNombreAndApellido(nombre, apellido).stream()
                .map(this::convertirAModeloDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EstudianteDTO> buscarPorCif(String cif) {
        return estudianteRepository.findByCif(cif).stream()
                .map(this::convertirAModeloDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarEstudiante(UUID id) {
        if (!estudianteRepository.existsById(id)) {
            throw new IllegalArgumentException("Estudiante no encontrado con ID: " + id);
        }
        estudianteRepository.deleteById(id);
    }

    // Método para convertir de Estudiante a EstudianteDTO
    private EstudianteDTO convertirAModeloDTO(Estudiante estudiante) {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setId(estudiante.getId());
        estudianteDTO.setNombre(estudiante.getNombre());
        estudianteDTO.setNombre2(estudiante.getNombre2());
        estudianteDTO.setApellido(estudiante.getApellido());
        estudianteDTO.setApellido2(estudiante.getApellido2());
        estudianteDTO.setCorreo(estudiante.getCorreo());
        estudianteDTO.setNumeroTelefono(estudiante.getNumeroTelefono());
        estudianteDTO.setCif(estudiante.getCif());
        return estudianteDTO;
    }

    private Estudiante convertirAEntidad(EstudianteDTO estudianteDTO) {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(estudianteDTO.getId());
        estudiante.setNombre(estudianteDTO.getNombre());
        estudiante.setNombre2(estudianteDTO.getNombre2());
        estudiante.setApellido(estudianteDTO.getApellido());
        estudiante.setApellido2(estudianteDTO.getApellido2());
        estudiante.setCorreo(estudianteDTO.getCorreo());
        estudiante.setNumeroTelefono(estudianteDTO.getNumeroTelefono());
        estudiante.setCif(estudianteDTO.getCif());
        return estudiante;
    }

}
