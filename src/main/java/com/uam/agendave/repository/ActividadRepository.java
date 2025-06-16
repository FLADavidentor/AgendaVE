package com.uam.agendave.repository;

import com.uam.agendave.model.Actividad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ActividadRepository extends JpaRepository<Actividad, UUID> {

    Page<Actividad> findAll(Pageable pageable);

    List<Actividad> findByEstado(Boolean estado);

    List<Actividad> findByNombreActividadId(UUID nombreActividadId);

    List<Actividad> findByNombre(String nombreActividad);

    List<Actividad> findByFechaBeforeAndEstadoTrue(LocalDate date);
}
