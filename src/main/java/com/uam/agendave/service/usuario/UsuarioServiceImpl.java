package com.uam.agendave.service.usuario;

import com.uam.agendave.dto.EstudianteDTO;
import com.uam.agendave.dto.LoginRequest;
import com.uam.agendave.dto.TestDTO;
import com.uam.agendave.mapper.EstudianteMapper;
import com.uam.agendave.model.ApiResponse;
import com.uam.agendave.model.Estudiante;
import com.uam.agendave.model.Usuario;
import com.uam.agendave.repository.EstudianteRepository;
import com.uam.agendave.repository.UsuarioRepository;
import com.uam.agendave.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final String BASE_URL = "https://uvirtual.uam.edu.ni:442/uambiblioapi/User/";

    private final RestClient restClient;
    private final EstudianteRepository estudianteRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstudianteMapper estudianteMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public UsuarioServiceImpl(RestClient restClient, EstudianteRepository estudianteRepository,
                              UsuarioRepository usuarioRepository, EstudianteMapper estudianteMapper) {
        this.restClient = restClient;
        this.estudianteRepository = estudianteRepository;
        this.usuarioRepository = usuarioRepository;
        this.estudianteMapper = estudianteMapper;
        this.jwtUtil = new JwtUtil();
    }

    @Transactional
    @Override
    public EstudianteDTO loginUsuario(LoginRequest loginRequest) {
        try {
            Optional<Estudiante> estudiante = estudianteRepository.findByCif(loginRequest.getCif());

            if (estudiante.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), estudiante.get().getPassword())) {
                return estudianteMapper.toDTO(estudiante.get());
            } else {
                String token = autenticarEstudiante(loginRequest);
                TestDTO estudianteData = obtenerInformacionEstudiante(token, loginRequest);

                Estudiante nuevoEstudiante = estudianteMapper.toEntity(estudianteData,loginRequest.getPassword());
                nuevoEstudiante.setPassword(passwordEncoder.encode(loginRequest.getPassword()));
                estudianteRepository.save(nuevoEstudiante);
                System.out.println("Saving new estudiante: " + nuevoEstudiante.getCif());
                estudianteRepository.save(nuevoEstudiante);


                return estudianteMapper.toDTO(nuevoEstudiante);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public String autenticarEstudiante(LoginRequest loginRequest) {
        String loginUrl = BASE_URL + "login";

        Map<String, String> requestBody = Map.of(
                "cif", loginRequest.getCif(),
                "password", loginRequest.getPassword()
        );

        try {
            return restClient.method(HttpMethod.POST)
                    .uri(loginUrl)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error HTTP: " + e.getStatusCode());
            System.err.println("Respuesta: " + e.getResponseBodyAsString());
            throw new RuntimeException("Error autenticando: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage(), e);
        }
    }

    @Override
    public TestDTO obtenerInformacionEstudiante(String token, LoginRequest loginRequest) {
        String infoUrl = BASE_URL + "GetStudentInformation?cif=" + loginRequest.getCif();

        try {
            ResponseEntity<ApiResponse> response = restClient.get()
                    .uri(infoUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .toEntity(ApiResponse.class);

            ApiResponse body = response.getBody();
            if (body != null && body.isSuccess() && !body.getData().isEmpty()) {
                System.out.println("response body: " + response.getBody());
                return body.getData().get(0);
            }
            return null;

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo datos del estudiante: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> loginUsuarioAdmin(LoginRequest loginRequest) {
        Optional<Usuario> adminOpt = usuarioRepository.findByCorreo(loginRequest.getCif());

        if (adminOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Administrador no encontrado"));
        }

        Usuario admin = adminOpt.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), admin.getContrasena())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Contraseña incorrecta"));
        }

        String token = jwtUtil.generateToken(admin.getCorreo(), "ADMIN");

        return ResponseEntity.ok(Map.of(
                "token", token,
                "rol", "ADMIN",
                "usuario", Map.of(
                        "correo", admin.getCorreo(),
                        "nombre", admin.getNombre(),
                        "apellido", admin.getApellido()
                )
        ));
    }

}