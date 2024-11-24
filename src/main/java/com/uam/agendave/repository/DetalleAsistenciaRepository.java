package com.uam.agendave.repository;

import com.uam.agendave.model.DetalleAsistencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DetalleAsistenciaRepository extends JpaRepository<DetalleAsistencia, UUID> {
    // Aquí puedes agregar consultas personalizadas si es necesario
    List<DetalleAsistencia> findByAsistenciaId(UUID asistenciaId);
    List<DetalleAsistencia> findByRegistroId(UUID registroId);
}
