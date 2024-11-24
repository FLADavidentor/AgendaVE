package com.uam.agendave.repository;

import com.uam.agendave.model.TipoActividad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TipoActividadRepository extends JpaRepository<TipoActividad, UUID> {
    // Agrega métodos personalizados si son necesarios
    List<TipoActividad> findByNombreTipoContainingIgnoreCase(String nombreTipo); // Búsqueda por nombre parcial
    List<TipoActividad> findByFacultadEncargadaContainingIgnoreCase(String facultad); // Búsqueda por facultad

}
