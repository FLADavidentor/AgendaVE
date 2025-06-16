package com.uam.agendave.repository;

import com.uam.agendave.model.Lugar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LugarRepository extends JpaRepository<Lugar, UUID> {
    // Agrega métodos personalizados si son necesarios
    List<Lugar> findByNombreContainingIgnoreCase(String nombre); // Búsqueda por nombre (parcial y sin distinción de mayúsculas)
    List<Lugar> findByCapacidadGreaterThan(int capacidad);       // Búsqueda por capacidad mayor a un valor

    // Busca actividades que empiecen con el texto dado
    List<Lugar> findByNombreStartingWith(String nombre);
    @Query("SELECT a.lugar FROM Actividad a WHERE a.id = :idActividad")
    Optional<Lugar> findLugarByActividadId(@Param("idActividad") UUID idActividad);

}
