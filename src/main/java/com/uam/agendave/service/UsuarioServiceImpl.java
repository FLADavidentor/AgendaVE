package com.uam.agendave.service;


import com.uam.agendave.dto.EstudianteDTO;
import com.uam.agendave.dto.LoginRequest;
import com.uam.agendave.dto.TestDTO;
import com.uam.agendave.dto.UsuarioDTO;
import com.uam.agendave.model.ApiResponse;
import com.uam.agendave.model.Estudiante;
import com.uam.agendave.repository.EstudianteRepository;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {


    private static final String BASE_URL = "https://uvirtual.uam.edu.ni:442/uambiblioapi/User/";
    private final RestClient restClient;
    private final EstudianteRepository estudianteRepository;

    public UsuarioServiceImpl(RestClient restClient, EstudianteRepository estudianteRepository) {
        this.restClient = restClient;
        this.estudianteRepository = estudianteRepository;
    }

    @Override
    public EstudianteDTO loginUsuario(LoginRequest loginRequest) {
        try {
            Optional<Estudiante> estudiante = estudianteRepository.findByCif(loginRequest.getCif());

            if (estudiante.isPresent()
                    && estudiante.get().getPassword().equals(loginRequest.getPassword())) {


                EstudianteDTO estudianteDTO = mapearEstudianteDTO(estudiante.get());
                return estudianteDTO;


            } else {
                String token = autenticarEstudiante(loginRequest);
                TestDTO estudianteData = obtenerInformacionEstudiante(token, loginRequest);

                Estudiante estudiantePersistencia = mapearAEstudiante(estudianteData, loginRequest.getPassword());

                estudianteRepository.save(estudiantePersistencia);
                EstudianteDTO estudianteDTO = mapearEstudianteDTO(estudiantePersistencia);

                return estudianteDTO;
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public String autenticarEstudiante(LoginRequest loginRequest) {
        String loginUrl = BASE_URL + "login";

        // Crear el cuerpo de la solicitud
        Map<String, String> requestBody = Map.of(
                "cif", loginRequest.getCif(),
                "password", loginRequest.getPassword()
        );

        try {
            // Realizar la solicitud POST con RestClient
            String token = restClient.method(HttpMethod.POST)
                    .uri(loginUrl)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            return token;

        } catch (HttpClientErrorException e) {
            // Manejo de errores 4xx
            System.err.println("Error HTTP (4xx): " + e.getStatusCode());
            System.err.println("Cuerpo de la respuesta: " + e.getResponseBodyAsString());
            throw new RuntimeException("Error de autenticación: " + e.getMessage(), e);
        } catch (HttpServerErrorException e) {
            // Manejo de errores 5xx
            System.err.println("Error HTTP (5xx): " + e.getStatusCode());
            System.err.println("Cuerpo de la respuesta: " + e.getResponseBodyAsString());
            throw new RuntimeException("Error en el servidor externo: " + e.getMessage(), e);
        } catch (Exception e) {
            // Manejo de errores generales
            System.err.println("Error inesperado al comunicarse con la API externa: " + e.getMessage());
            throw new RuntimeException("Error desconocido: " + e.getMessage(), e);
        }
    }

    @Override
    public TestDTO obtenerInformacionEstudiante(String token, LoginRequest loginRequest) {
        String infoUrl = BASE_URL + "GetStudentInformation?cif=" + loginRequest.getCif();

        try {
            // Solicitud GET para obtener información del estudiante
            ResponseEntity<ApiResponse> response = restClient.get()
                    .uri(infoUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .toEntity(ApiResponse.class);

            ApiResponse body = response.getBody();
            if (body != null && body.isSuccess() && !body.getData().isEmpty()) {
                TestDTO estudiante = body.getData().get(0);
                return estudiante;
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener información del estudiante: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> loginUsuarioAdmin(LoginRequest loginRequest) {
        try {
            Optional<Estudiante> estudiante = estudianteRepository.findByCif(loginRequest.getCif());

            if (estudiante.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estudiante no encontrado");
            }
            if (!estudiante.get().getPassword().equals(loginRequest.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
            }
            if (!estudiante.get().getTipo().equals("Admin")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("no tiene privilegios");
            }

            EstudianteDTO estudianteDTO = mapearEstudianteDTO(estudiante.get());
            return ResponseEntity.status(HttpStatus.OK).body(estudianteDTO);


        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    private Estudiante mapearAEstudiante(TestDTO estudianteData, String password) {
        Estudiante estudiante = new Estudiante();

        String tel = estudianteData.getTelefono() != null ? estudianteData.getTelefono().toString() : "";
        String tipo = estudianteData.getTipo() != null ? estudianteData.getTipo().toString() : "";
        String correo = estudianteData.getCorreo() != null ? estudianteData.getCorreo().toString() : "";
        String sexo = estudianteData.getSexo() != null ? estudianteData.getSexo().toString() : "";
        String carrera = estudianteData.getCarrera() != null ? estudianteData.getCarrera().toString() : "";
        String facultad = estudianteData.getFacultad() != null ? estudianteData.getFacultad().toString() : "";


        estudiante.setCif((String) estudianteData.getCif());
        estudiante.setNombres((String) estudianteData.getNombres());
        estudiante.setApellidos((String) estudianteData.getApellidos());

        estudiante.setTipo(tipo);
        estudiante.setCorreo(correo);
        estudiante.setSexo(sexo);
        estudiante.setTelefono(tel);
        estudiante.setCarrera(carrera);
        estudiante.setFacultad(facultad);
        estudiante.setPassword(password);

        return estudiante;
    }

    private EstudianteDTO mapearEstudianteDTO(Estudiante estudiante) {
        EstudianteDTO estudianteDTO = new EstudianteDTO();

        estudianteDTO.setApellido(estudiante.getApellidos());
        estudianteDTO.setNombre(estudiante.getNombres());
        estudianteDTO.setCif(estudiante.getCif());
        estudianteDTO.setCorreo(estudiante.getCorreo());
        estudianteDTO.setFacultad(estudiante.getFacultad());
        estudianteDTO.setCarrera(estudiante.getCarrera());

        return estudianteDTO;


    }

}
