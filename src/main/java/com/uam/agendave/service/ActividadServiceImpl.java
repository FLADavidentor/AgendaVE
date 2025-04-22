package com.uam.agendave.service;

import com.uam.agendave.dto.ActividadDTO;
import com.uam.agendave.dto.ImagenDTO;
import com.uam.agendave.model.*;
import com.uam.agendave.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ActividadServiceImpl implements ActividadService {

    private final ActividadRepository actividadRepository;
    private final NombreActividadRepository nombreActividadRepository;
    private final NombreActividadService nombreActividadService;
    private final LugarRepository lugarRepository;
    private final RegistroRepository registroRepository;

    public ActividadServiceImpl(
            ActividadRepository actividadRepository,
            NombreActividadRepository nombreActividadRepository,
            LugarRepository lugarRepository,
            RegistroRepository registroRepository) {
        this.actividadRepository = actividadRepository;
        this.nombreActividadRepository = nombreActividadRepository;
        this.nombreActividadService = new NombreActividadServiceImpl(nombreActividadRepository);
        this.lugarRepository = lugarRepository;
        this.registroRepository = registroRepository;
    }

    @Override
    public List<ActividadDTO> obtenerTodas() {
        // Obtener todas las actividades desde el repositorio
        List<Actividad> actividades = actividadRepository.findAll();

        // Convertir las entidades a DTOs
        return actividades.stream().map(actividad -> {

            ActividadDTO actividadDTO = new ActividadDTO();

            ImageData imgData = actividad.getImagen();
            if (imgData != null
                    && imgData.getImagenBase64() != null
                    && !imgData.getImagenBase64().trim().isEmpty()) {
                ImagenDTO imagenDTO = new ImagenDTO();
                imagenDTO.setNombre(actividad.getNombre());
                imagenDTO.setImagenBase64(imgData.getImagenBase64());
                actividadDTO.setImagen(imagenDTO);
            } else {
                // Opcional: asegurarte de que la entidad actividad permita imagen null
                actividadDTO.setImagen(null);
            }

            actividadDTO.setId(actividad.getId());
            actividadDTO.setDescripcion(actividad.getDescripcion());
            actividadDTO.setFecha(actividad.getFecha());
            actividadDTO.setHoraInicio(actividad.getHoraInicio());
            actividadDTO.setHoraFin(actividad.getHoraFin());
            actividadDTO.setEstado(actividad.isEstado());
            actividadDTO.setCupo(actividad.getCupo());


            // Manejar las relaciones: convertir IDs a nombres
            actividadDTO.setNombreActividad(actividad.getNombreActividad() != null
                    ? actividad.getNombreActividad().getNombre()
                    : "Nombre no especificado");
            actividadDTO.setLugar(actividad.getLugar() != null
                    ? actividad.getLugar().getNombre()
                    : "Lugar no especificado");

            // Manejar convalidaciones
            actividadDTO.setConvalidacionesPermitidas(actividad.getConvalidacionesPermitidas());
            actividadDTO.setTotalConvalidacionesPermitidas(actividad.getTotalConvalidacionesPermitidas());

            return actividadDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void guardarActividad(ActividadDTO actividadDTO) throws Exception {

        try {
            // Buscar o crear NombreActividad
            NombreActividad nombreActividad = nombreActividadRepository.findByNombre(actividadDTO.getNombreActividad())
                    .stream()
                    .findFirst()
                    .orElseGet(() -> {
                        NombreActividad nuevaNombreActividad = new NombreActividad();
                        nuevaNombreActividad.setNombre(actividadDTO.getNombreActividad());
                        return nombreActividadService.guardar(nuevaNombreActividad); // Usar el servicio para persistir
                    });
            System.out.println(nombreActividad.getId());

            Lugar lugar = lugarRepository.findByNombreContainingIgnoreCase(actividadDTO.getLugar())
                    .stream().findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Lugar no encontrado: " + actividadDTO.getLugar()));
            System.out.println(lugar.getId());

            Actividad actividad = getActividad(actividadDTO, nombreActividad, lugar);

            actividadRepository.save(actividad);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    private static Actividad getActividad(ActividadDTO actividadDTO, NombreActividad nombreActividad, Lugar lugar) {
        Actividad actividad = new Actividad();

        ImagenDTO imgDto = actividadDTO.getImagen();
        if (imgDto != null
                && imgDto.getImagenBase64() != null
                && !imgDto.getImagenBase64().trim().isEmpty()) {

            ImageData imageData = new ImageData();
            imageData.setNombre(imgDto.getNombre());
            imageData.setImagenBase64(imgDto.getImagenBase64());
            System.out.println(imageData.getImagenBase64());
            actividad.setImagen(imageData);
            System.out.println(actividad.getImagen().getImagenBase64());

        } else {
            // Opcional: asegurarte de que la entidad actividad permita imagen null
            actividad.setImagen(null);
        }

        actividad.setNombre(nombreActividad.getNombre());
        actividad.setDescripcion(actividadDTO.getDescripcion());
        actividad.setFecha(actividadDTO.getFecha());
        actividad.setHoraInicio(actividadDTO.getHoraInicio());
        actividad.setHoraFin(actividadDTO.getHoraFin());
        actividad.setCupo(actividadDTO.getCupo());
        actividad.setNombreActividad(nombreActividad);
        actividad.setLugar(lugar);

        actividad.setConvalidacionesPermitidas(actividadDTO.getConvalidacionesPermitidas());
        actividad.setTotalConvalidacionesPermitidas(actividadDTO.getTotalConvalidacionesPermitidas());

        return actividad;
    }


    @Override
    public Actividad buscarPorId(UUID id) {
        return actividadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada con ID: " + id));
    }

    @Override
    public ActividadDTO actualizarActividad(ActividadDTO actividadDTO) {
        // Buscar la actividad a actualizar por ID
        Actividad actividadExistente = actividadRepository.findById(actividadDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("La actividad a actualizar no existe con ID: " + actividadDTO.getId()));

        // Comprobar si el nombre de la actividad ha cambiado
        if (!actividadDTO.getNombreActividad().equals(actividadExistente.getNombreActividad())) {
            // Buscar el NombreActividad por nombre
            NombreActividad nombreActividad = nombreActividadRepository.findByNombre(actividadDTO.getNombreActividad())
                    .stream()
                    .findFirst()
                    .orElseGet(() -> {
                        // Si no existe, crear uno nuevo
                        NombreActividad nuevoNombreActividad = new NombreActividad();
                        nuevoNombreActividad.setNombre(actividadDTO.getNombreActividad());
                        return nombreActividadRepository.save(nuevoNombreActividad);
                    });

            // Actualizar el NombreActividad en la actividad
            actividadExistente.setNombreActividad(nombreActividad);
        }

        // Actualizar los campos simples de la actividad
        actividadExistente.setDescripcion(actividadDTO.getDescripcion());
        actividadExistente.setFecha(actividadDTO.getFecha());
        actividadExistente.setHoraInicio(actividadDTO.getHoraInicio());
        actividadExistente.setHoraFin(actividadDTO.getHoraFin());
        actividadExistente.setCupo(actividadDTO.getCupo());

        // Buscar y asignar Lugar
        Lugar lugar = lugarRepository.findByNombreContainingIgnoreCase(actividadDTO.getLugar())
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Lugar no encontrado: " + actividadDTO.getLugar()));
        actividadExistente.setLugar(lugar);

        // Guardar los cambios
        Actividad actividadActualizada = actividadRepository.save(actividadExistente);

        // Convertir a DTO y retornar
        return convertirAModelDTO(actividadActualizada);
    }



    @Override
    public void eliminarActividad(UUID id) {
        if (!actividadRepository.existsById(id)) {
            throw new IllegalArgumentException("Actividad no encontrada con ID: " + id);
        }
        actividadRepository.deleteById(id);
    }

    @Override
    public List<ActividadDTO> buscarPorNombre(String nombre) {
        List<Actividad> actividades = actividadRepository.findByNombreActividadNombre(nombre);
        return actividades.stream()
                .map(this::convertirAModelDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ActividadDTO> buscarPorLugar(UUID idLugar) {
        List<Actividad> actividades = actividadRepository.findByLugarId(idLugar);
        return actividades.stream()
                .map(this::convertirAModelDTO)
                .collect(Collectors.toList());
    }

    @Override
    public int getCupoRestante(UUID actividadID) {
        Actividad act = actividadRepository.findById(actividadID)
                .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada con ID: " + actividadID));
        long inscritos = registroRepository.countByActividadId(actividadID);
        return act.getCupo() - (int) inscritos;
    }

    @Override
    public List<ActividadDTO> buscarActividadesConCupoDisponible() {
        List<Actividad> actividades = actividadRepository.findActividadesConCupoDisponible();
        return actividades.stream()
                .map(this::convertirAModelDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Map<TipoConvalidacion, Integer> obtenerConvalidacionesPorActividad(UUID id) {
        List<Object[]> resultados = actividadRepository.findConvalidacionesById(id);

        // Convertir la lista en un mapa
        return resultados.stream()
                .collect(Collectors.toMap(
                        resultado -> (TipoConvalidacion) resultado[0], // Clave (TipoConvalidacion)
                        resultado -> (Integer) resultado[1]           // Valor (cantidadPermitida)
                ));
    }


    @Override
    public Integer obtenerTotalConvalidacionesMaximas(UUID id) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada con ID: " + id));
        return actividad.getTotalConvalidacionesPermitidas();
    }


    private ActividadDTO convertirAModelDTO(Actividad actividad) {
        ActividadDTO actividadDTO = new ActividadDTO();
        actividadDTO.setId(actividad.getId());
        actividadDTO.setDescripcion(actividad.getDescripcion());
        actividadDTO.setFecha(actividad.getFecha());
        actividadDTO.setHoraInicio(actividad.getHoraInicio());
        actividadDTO.setHoraFin(actividad.getHoraFin());
        actividadDTO.setEstado(actividad.isEstado());
        actividadDTO.setCupo(actividad.getCupo());

        // Convertir relaciones a nombres
        actividadDTO.setNombreActividad(actividad.getNombreActividad() != null ? actividad.getNombreActividad().getNombre() : null);
        actividadDTO.setLugar(actividad.getLugar() != null ? actividad.getLugar().getNombre() : null);

        // Convalidaciones
        actividadDTO.setConvalidacionesPermitidas(actividad.getConvalidacionesPermitidas());
        actividadDTO.setTotalConvalidacionesPermitidas(actividad.getTotalConvalidacionesPermitidas());

        return actividadDTO;
    }

}
