package com.uam.agendave.repository;

import com.uam.agendave.model.Lugar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LugarRepository extends JpaRepository<Lugar, UUID> {
    // Agrega métodos personalizados si son necesarios
    List<Lugar> findByNombreContainingIgnoreCase(String nombre); // Búsqueda por nombre (parcial y sin distinción de mayúsculas)
    List<Lugar> findByCapacidadGreaterThan(int capacidad);       // Búsqueda por capacidad mayor a un valor

}
