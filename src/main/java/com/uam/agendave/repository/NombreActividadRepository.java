package com.uam.agendave.repository;

import com.uam.agendave.model.NombreActividad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NombreActividadRepository extends JpaRepository<NombreActividad, UUID> {

    // Buscar por nombre exacto
    List<NombreActividad> findByNombre(String nombre);

    // Buscar por parte del nombre (case-insensitive)
    List<NombreActividad> findByNombreContainingIgnoreCase(String nombre);

    // Buscar por tipo de actividad
    List<NombreActividad> findByTipoActividadId(UUID idTipoActividad);

}
