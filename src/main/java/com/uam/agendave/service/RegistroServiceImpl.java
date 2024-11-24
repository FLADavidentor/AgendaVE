package com.uam.agendave.service;

import com.uam.agendave.model.Registro;
import com.uam.agendave.repository.RegistroRepository;
import com.uam.agendave.service.RegistroService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RegistroServiceImpl implements RegistroService {

    private final RegistroRepository registroRepository;

    public RegistroServiceImpl(RegistroRepository registroRepository) {
        this.registroRepository = registroRepository;
    }

    @Override
    public List<Registro> obtenerTodos() {
        return registroRepository.findAll();
    }

    @Override
    public Registro guardarRegistro(Registro registro) {
        // Validar lógica relacionada a convalidaciones o transporte aquí, si aplica
        return registroRepository.save(registro);
    }

    @Override
    public Registro buscarPorId(UUID id) {
        return registroRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Registro no encontrado con ID: " + id));
    }

    @Override
    public void eliminarRegistro(UUID id) {
        if (!registroRepository.existsById(id)) {
            throw new IllegalArgumentException("Registro no encontrado con ID: " + id);
        }
        registroRepository.deleteById(id);
    }

    @Override
    public List<Registro> buscarPorEstudiante(UUID idEstudiante) {
        return registroRepository.findByEstudianteId(idEstudiante);
    }

    @Override
    public List<Registro> buscarPorActividad(UUID idActividad) {
        return registroRepository.findByActividadId(idActividad);
    }
}
