# 🚀 Smart ATS & AI Interviewer

Un Sistema de Seguimiento de Candidatos (ATS) moderno y full-stack potenciado por Inteligencia Artificial. Este proyecto automatiza el primer filtro de contratación: analiza currículums en PDF, realiza un "match" inteligente de compatibilidad con ofertas de trabajo mediante Gemini AI, y **permite a las empresas realizar entrevistas técnicas interactivas automatizadas por chat**.

## 🎯 Por qué este proyecto
Desarrollado para demostrar habilidades en la integración de flujos de negocio tradicionales (Spring Boot, Spring Security con JWT, JPA) con capacidades de IA generativa, aplicando una arquitectura limpia, control de roles (`ROLE_EMPRESA`, `ROLE_CANDIDATO`) y una experiencia de usuario ágil.

## ⚙️ Stack Tecnológico
* **Backend:** Java 17, Spring Boot 3, Spring Security (JWT), Spring Data JPA.
* **Frontend:** HTML5, Bootstrap 5, JavaScript (Vanilla JS con Fetch API).
* **Base de Datos:** PostgreSQL / H2.
* **IA / Agentes:** Google AI Gemini (gemini-1.5-flash) integrado nativamente mediante peticiones REST HTTP (`RestTemplate`).
* **Procesamiento de Archivos:** Apache PDFBox (Extracción de texto en PDFs).
* **Documentación:** Swagger / OpenAPI 3.

## 🏗️ Arquitectura y Flujo
El proyecto está diseñado bajo una arquitectura desacoplada y basada en eventos de usuario:
1. **Autenticación Segura:** El sistema emite un token JWT que codifica el rol del usuario para redirigirlo automáticamente a su panel correspondiente (`panel-empresa.html` o `panel-candidato.html`).
2. **Gestión de Ofertas:** Las empresas publican puestos de trabajo con requisitos detallados.
3. **Procesamiento de CVs:** Los candidatos suben su currículum en PDF; el backend extrae el texto automáticamente y lo asocia al perfil.
4. **Matching por IA:** Al postularse, Gemini evalúa la oferta frente al texto crudo del CV, devolviendo un porcentaje de compatibilidad y un resumen analítico.
5. **Entrevistas Técnicas Inteligentes (🤖 *Killer Feature*):** Las empresas pueden activar una entrevista para los mejores candidatos. El sistema abre una sala de chat donde Gemini, asumiendo el rol de Tech Lead, realiza preguntas técnicas en tiempo real. Al terminar, **la IA cierra la entrevista automáticamente y genera una evaluación final silenciosa** que actualiza la nota del candidato en el panel de la empresa.

## 🧪 Datos y Usuarios de Prueba
Para facilitar las pruebas del sistema sin tener que registrar perfiles desde cero, el repositorio incluye una carpeta llamada `Usuarios de Prueba`. Dentro encontrarás:
* Archivos `.txt` con credenciales de prueba listas para usar (roles de Empresa y Candidato).
* Textos de ejemplo para publicar Ofertas de trabajo realistas.
* Currículums de prueba en formato `.pdf` listos para ser subidos y procesados por el motor de IA.

## 🔑 Variables de Entorno (Backend)
Antes de ejecutar el proyecto, configura la siguiente variable en tu sistema o IDE (`application.properties`):
* `GEMINI_API_KEY`: Tu clave privada generada en Google AI Studio.
* `GEMINI_API_URL`: `https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent`

## 🚀 Cómo ejecutarlo en local

### 1. El Backend (Spring Boot)
1. Clona este repositorio y ábrelo en tu IDE (ej. IntelliJ IDEA).
2. Configura tu `GEMINI_API_KEY` en los `application.properties` o variables de entorno.
3. Ejecuta la aplicación Spring Boot desde la clase principal (`AtsApiApplication`). El servidor correrá en `http://localhost:8080`.

### 2. El Frontend (Interfaz Web)
El frontend se comunica con la API mediante peticiones asíncronas seguras:
* **Opción A (Recomendada para desarrollo):** Abre la carpeta del frontend usando la extensión **Live Server** en VS Code para simular un servidor local en el puerto 5500.
* **Opción B (Integrado):** Mueve los archivos HTML estáticos a la carpeta `src/main/resources/static/` del backend y accede vía `http://localhost:8080/login.html`.
*(Nota: Evita abrir los HTML directamente con doble clic para no sufrir bloqueos de seguridad CORS `file://` del navegador).*

## 📡 Endpoints Principales
* **Autenticación:** `POST /api/auth/login` y `POST /api/auth/registro` (Control de tokens y roles).
* **Ofertas:** `GET /api/ofertas` y `POST /api/ofertas` (Gestión para reclutadores).
* **CVs:** `POST /api/cvs/upload` (Sube y extrae texto de PDFs de forma segura por usuario autenticado).
