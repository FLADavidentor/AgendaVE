# 📘 Documentación Técnica - Backend AgendaVE

## ✨ Descripción General
Este proyecto corresponde al backend del sistema AgendaVE, una aplicación desarrollada en Java con Spring Boot, orientada a la gestión de actividades universitarias, inscripciones y convalidaciones de créditos.

---

## 🔧 Tecnologías Usadas
- Spring Boot 3+
- Spring Data JPA
- PostgreSQL
- Lombok
- Spring Security (básico con JWT externo)
- JavaMailSender
- Jackson

---

## 🛁 Estructura Principal

| Módulo           | Descripción breve |
|------------------|------------------|
| `model/`         | Entidades persistentes (JPA) |
| `dto/`           | Data Transfer Objects (puente entre modelo y frontend) |
| `repository/`    | Interfaces JPA para acceso a datos |
| `service/`       | Servicios con lógica de negocio |
| `config/`        | Beans, seguridad, REST y JSON config |
| `util/`          | Filtros, deserializadores, helpers JWT |

---

## 📃 Modelos (`model`)
Entidades JPA con identificadores `UUID` heredados de `Identifiable.java`.

- **Actividad:** Datos completos de una actividad, incluyendo lugar, cupo, fecha y convalidaciones
- **Administrador / Estudiante:** Usuarios del sistema
- **Lugar / NombreActividad:** Tablas de referencia
- **Registro:** Inscripciones por estudiante a actividades con asistencia, tipo de convalidación y timestamp
- **Enums:** `EstadoAsistencia`, `TipoConvalidacion`
- **Otros:** `ImageData` para almacenar imágenes base64

---

## 📂 DTOs (`dto`)
Objetos para transportar datos entre frontend y backend, sin exponer directamente las entidades.

- `ActividadDTO`, `RegistroDTO`, `LugarDTO`, etc.
- `LoginRequest`, `UsuarioDTO`, `EstudianteDTO` para autenticación
- `ImagenDTO` incluye nombre y cadena base64

---

## 🔄 Repositorios (`repository`)
Interfaces que extienden `JpaRepository`, permitiendo consultas automáticas y personalizadas.

- `ActividadRepository`: incluye paginación y búsqueda por nombreActividad
- `RegistroRepository`: permite contar, eliminar y buscar registros por estudiante/actividad
- `LugarRepository`, `NombreActividadRepository`: con funciones de búsqueda parcial
- Repositorios comentados: `ConvalidacionRepository`, `AsistenciaRepository`

---

## 🛠️ Servicios (`service`)
Contienen la lógica de negocio. Implementaciones están en clases `ServiceImpl`.

- `ActividadService`: CRUD, manejo de convalidaciones, imagen, cupo, inscripciones
- `RegistroService`: manejo de inscripciones, asistencia, y cálculo de créditos presentes
- `LugarService`, `NombreActividadService`: CRUD básico
- `UsuarioService`: login local o sincronizado con API externa de la UAM
- `EmailService`: envío asincrónico de correos de confirmación

---

## 🚼 Seguridad (`config.SecurityConfig` y `util.JwtFilter`)
- Se desactiva CSRF y se permite todo por defecto (para pruebas o modo "inseguro")
- `JwtFilter`: intercepta las peticiones y consulta la API externa para validar el token
- `JwtUtil`: clase auxiliar para almacenar el token sin validarlo

---

## 💡 Utilidades (`util`)
- `ApiResponse`: clase envoltorio para respuestas JSON
- `TipoConvalidacionKeyDeserializer`: permite deserializar mapas con enums como clave

---

## ⚙️ Configuración (`application.properties`)
- Puerto: `8181`
- Contexto: `/api`
- Base de datos PostgreSQL (host local, puerto `5433`)
- Correo habilitado con cuenta de Gmail (modo pruebas)
- Logs en modo debug para web y conversores JSON

---

## 🔐 Recomendaciones de Seguridad (para producción)
- Mover `spring.mail.password` a variable de entorno
- Definir reglas de autorización en `HttpSecurity`
- Validar JWTs localmente si se requiere confianza cero

---

## 🚀 Estado de Documentación
| Sección                  | Estado |
|---------------------------|--------|
| Modelos                   | ✅     |
| DTOs                      | ✅     |
| Repositorios              | ✅     |
| Servicios                 | ✅     |
| Utilidades                | ✅     |
| Configuración / Seguridad | ✅     |

---

📅 Documento generado el 25/04/2025 por solicitud del SpringBoot master 🧙‍♂️

