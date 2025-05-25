package com.uam.agendave.repository;

import com.uam.agendave.model.Registro;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RegistroRepository extends JpaRepository<Registro, UUID> {

    List<Registro> findByEstudianteCif(String cif);

    List<Registro> findByActividadId(UUID idActividad);

    long countByActividadId(UUID idActividad);

    Optional<Registro> findByEstudianteCifAndActividadId(String cif, UUID idActividad); // ✅ WORKS
    @Transactional
    void deleteByActividadId(UUID idActividad);
}