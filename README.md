# 🚀 Smart ATS & AI Interviewer

Un Sistema de Seguimiento de Candidatos (ATS) moderno y full-stack potenciado por Inteligencia Artificial. Este proyecto automatiza el primer filtro de contratación: analiza currículums en PDF, realiza un "match" inteligente de compatibilidad con ofertas de trabajo mediante Gemini AI y ofrece interfaces visuales diferenciadas para Empresas y Candidatos.

## 🎯 Por qué este proyecto
Desarrollado para demostrar habilidades en la integración de flujos de negocio tradicionales (Spring Boot, Spring Security con JWT, JPA) con capacidades de IA generativa, aplicando una arquitectura limpia, control de roles (`ROLE_EMPRESA`, `ROLE_CANDIDATO`) y una experiencia de usuario ágil.

## ⚙️ Stack Tecnológico
* **Backend:** Java 17, Spring Boot 3, Spring Security (JWT), Spring Data JPA
* **Frontend:** HTML5, Bootstrap 5, JavaScript (Vanilla JS con Fetch API)
* **Base de Datos:** PostgreSQL / H2
* **IA / Agentes:** Google AI Gemini (`gemini-1.5-flash` vía LangChain4j)
* **Procesamiento de Archivos:** Apache PDFBox (Extracción de texto en PDFs)
* **Documentación:** Swagger / OpenAPI 3

## 🏗️ Arquitectura y Flujo
El proyecto está diseñado bajo una arquitectura desacoplada:
1. **Autenticación Segura:** El sistema emite un token JWT que codifica el rol del usuario para redirigirlo automáticamente a su panel correspondiente (`panel-empresa.html` o `panel-candidato.html`).
2. **Gestión de Ofertas:** Las empresas publican puestos de trabajo con requisitos detallados.
3. **Procesamiento de CVs:** Los candidatos suben su currículum en PDF; el backend extrae el texto automáticamente y lo asocia al perfil.
4. **Matching por IA:** Al postularse, Gemini evalúa la oferta frente al texto crudo del CV, devolviendo un porcentaje de compatibilidad y un resumen analítico.

## 🔑 Variables de Entorno (Backend)
Antes de ejecutar el proyecto, configura la siguiente variable en tu sistema o IDE:
* `GEMINI_API_KEY`: Tu clave privada generada en [Google AI Studio](https://aistudio.google.com/).

## 🚀 Cómo ejecutarlo en local

### 1. El Backend (Spring Boot)
1. Clona este repositorio y ábrelo en tu IDE (ej. IntelliJ IDEA).
2. Configura tu `GEMINI_API_KEY` en los `application.properties` o variables de entorno.
3. Ejecuta la aplicación Spring Boot desde la clase principal (`AtsApiApplication`). El servidor correrá en `http://localhost:8080`.

### 2. El Frontend (Interfaz Web)
El frontend es completamente independiente y se comunica con la API mediante CORS:
1. Localiza la carpeta del frontend en tu repositorio.
2. Abre el archivo **`registro.html`** directamente en tu navegador (o usa un servidor local como *Live Server* en VS Code).
3. Regístrate como **Empresa** para publicar ofertas y ver candidatos, o como **Candidato** para subir tu CV en PDF y postularte.

🔗 **[Swagger UI (Documentación Backend)](http://localhost:8080/swagger-ui/index.html)**

## 📡 Endpoints Principales
* **Autenticación:** `POST /api/auth/login` y `POST /api/auth/registro` (Control de tokens y roles).
* **Ofertas:** `GET /api/ofertas` y `POST /api/ofertas` (Gestión para reclutadores).
* **CVs:** `POST /api/cvs/upload` (Sube y extrae texto de PDFs de forma segura por usuario autenticado).
* **Postulaciones e IA:** `GET /api/postulaciones/empresa/ofertas/{ofertaId}/candidatos` (Ranking filtrado de candidatos según la compatibilidad calculada por Gemini).
