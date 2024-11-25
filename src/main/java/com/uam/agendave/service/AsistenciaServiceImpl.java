package com.uam.agendave.service;

import com.uam.agendave.model.Asistencia;
import com.uam.agendave.repository.AsistenciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AsistenciaServiceImpl implements AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;

    public AsistenciaServiceImpl(AsistenciaRepository asistenciaRepository) {
        this.asistenciaRepository = asistenciaRepository;
    }

    @Override
    public Asistencia guardarAsistencia(Asistencia asistencia) {
        // Puedes agregar lógica adicional si es necesario antes de guardar
        return asistenciaRepository.save(asistencia);
    }

    @Override
    public Asistencia buscarPorId(UUID id) {
        return asistenciaRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Asistencia no encontrada con ID: " + id));
    }

    @Override
    public void eliminarAsistencia(UUID id) {
        if (!asistenciaRepository.existsById(id)) {
            throw new IllegalArgumentException("Asistencia no encontrada con ID: " + id);
        }
        asistenciaRepository.deleteById(id);
    }

    @Override
    public List<Asistencia> obtenerTodas() {
        // Utiliza el repositorio para obtener todas las asistencias desde la base de datos
        return asistenciaRepository.findAll();
    }

    // Buscar asistencia por ID de actividad
    @Override
    public List<Asistencia> buscarPorActividadId(UUID actividadId) {
        return asistenciaRepository.findByActividadId(actividadId);
    }
}