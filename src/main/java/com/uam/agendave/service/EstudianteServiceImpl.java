package com.uam.agendave.service;

import com.uam.agendave.dto.LoginRequest;
import com.uam.agendave.model.Estudiante;
import com.uam.agendave.util.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class EstudianteServiceImpl implements EstudianteService {

    private static final String BASE_URL = "https://uvirtual.uam.edu.ni:442/uambiblioapi/User/";
    private final RestClient restClient;

    public EstudianteServiceImpl(RestClient restClient) {
        this.restClient = restClient;
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
    public String obtenerInformacionEstudiante(String token, LoginRequest loginRequest) {
        String infoUrl = BASE_URL + "GetStudentInformation?cif=" + loginRequest.getCif();

        try {
            // Solicitud GET para obtener información del estudiante
            ResponseEntity<String> response = restClient.get()
                    .uri(infoUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .toEntity(String.class);

            // Retornar directamente la respuesta del API externo
            return response.getBody();

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener información del estudiante: " + e.getMessage(), e);
        }
    }



    private Estudiante mapearAEstudiante(Map<String, Object> estudianteData) {
        Estudiante estudiante = new Estudiante();
        estudiante.setCif((String) estudianteData.get("cif"));
        estudiante.setNombres((String) estudianteData.get("nombres"));
        estudiante.setApellidos((String) estudianteData.get("apellidos"));
        estudiante.setTipo((String) estudianteData.get("tipo"));
        estudiante.setCorreo((String) estudianteData.get("correo"));
        estudiante.setSexo((String) estudianteData.get("sexo"));
        estudiante.setTelefono((String) estudianteData.get("telefono"));
        estudiante.setCarrera((String) estudianteData.get("carrera"));
        estudiante.setFacultad((String) estudianteData.get("facultad"));
        return estudiante;
    }
}


