package com.uam.agendave.repository;

import com.uam.agendave.model.Transporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransporteRepository extends JpaRepository<Transporte, UUID> {
    List<Transporte> findByLugarNombreContainingIgnoreCase(String nombre);
    Optional<Transporte> findByLugarNombreIgnoreCaseAndCapacidad(String nombre, Integer capacidad);

}
