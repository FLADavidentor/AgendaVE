package com.uam.agendave.service.Registro;

import com.uam.agendave.model.Estudiante;
import com.uam.agendave.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistroHelper {

    private static EstudianteRepository estudianteRepository;

    @Autowired
    public RegistroHelper(EstudianteRepository repo) {
        RegistroHelper.estudianteRepository = repo;
    }

    public static Estudiante getEstudianteByCif(String cif) {
        return estudianteRepository.findByCif(cif)
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado con CIF: " + cif));
    }
}


