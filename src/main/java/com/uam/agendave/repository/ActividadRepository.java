package com.uam.agendave.repository;

import com.uam.agendave.dto.ActividadDTO;
import com.uam.agendave.model.Actividad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ActividadRepository extends JpaRepository<Actividad, UUID> {

    Page<Actividad> findAll(Pageable pageable);

    List<Actividad> findByEstado(Boolean estado);

    List<Actividad> findByNombreActividadId(UUID nombreActividadId);


}
