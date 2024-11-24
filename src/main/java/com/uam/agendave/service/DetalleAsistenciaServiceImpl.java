package com.uam.agendave.service;

import com.uam.agendave.model.DetalleAsistencia;
import com.uam.agendave.repository.DetalleAsistenciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DetalleAsistenciaServiceImpl implements DetalleAsistenciaService {

    private final DetalleAsistenciaRepository detalleAsistenciaRepository;

    public DetalleAsistenciaServiceImpl(DetalleAsistenciaRepository detalleAsistenciaRepository) {
        this.detalleAsistenciaRepository = detalleAsistenciaRepository;
    }

    @Override
    public List<DetalleAsistencia> obtenerTodos() {
        return List.of();
    }

    @Override
    public DetalleAsistencia guardarDetalleAsistencia(DetalleAsistencia detalleAsistencia) {
        return detalleAsistenciaRepository.save(detalleAsistencia);
    }

    @Override
    public DetalleAsistencia buscarPorId(UUID id) {
        return detalleAsistenciaRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("DetalleAsistencia no encontrado con ID: " + id));
    }

    @Override
    public void eliminarDetalleAsistencia(UUID id) {
        if (!detalleAsistenciaRepository.existsById(id)) {
            throw new IllegalArgumentException("DetalleAsistencia no encontrado con ID: " + id);
        }
        detalleAsistenciaRepository.deleteById(id);
    }

    @Override
    public List<DetalleAsistencia> obtenerPorAsistenciaId(UUID asistenciaId) {
        return detalleAsistenciaRepository.findByAsistenciaId(asistenciaId);
    }

    @Override
    public List<DetalleAsistencia> obtenerPorRegistroId(UUID registroId) {
        return detalleAsistenciaRepository.findByRegistroId(registroId);
    }
}
