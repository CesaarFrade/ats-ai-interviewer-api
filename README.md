# 🚀 Smart ATS & AI Interviewer API

Un Sistema de Seguimiento de Candidatos (ATS) potenciado por Inteligencia Artificial. Este proyecto backend automatiza el primer filtro de contratación: analiza currículums (PDFs), hace un "match" con ofertas de trabajo y realiza entrevistas técnicas autónomas mediante agentes de IA.

## 🎯 Por qué este proyecto
Desarrollado para demostrar habilidades en la integración de flujos de negocio tradicionales con capacidades de IA generativa, aplicando buenas prácticas de desarrollo y arquitectura.

## ⚙️ Stack Tecnológico
* **Core:** Java 17, Spring Boot 3
* **Base de Datos:** PostgreSQL / H2
* **IA / Agentes:** API de Google Gemini (gemini-2.5-flash)
* **Procesamiento de Archivos:** Apache PDFBox (Extracción de texto)
* **Arquitectura:** Arquitectura Hexagonal / Multicapa
* **Documentación:** Swagger / OpenAPI 3

## 🏗️ Arquitectura y Flujo
*(Aquí añadiremos un diagrama usando Mermaid.js cuando tengamos claro el diseño).*

1. El candidato sube su CV (PDF).
2. El sistema extrae texto y evalúa el perfil contra la oferta.
3. Si el *match* es > 70%, un Agente IA entrevista al candidato vía chat.

## 🔑 Variables de Entorno
Antes de ejecutar el proyecto, asegúrate de configurar las siguientes variables de entorno en tu sistema o IDE para proteger las credenciales:
* `GEMINI_API_KEY`: Tu clave privada generada en Google AI Studio.

## 🚀 Cómo ejecutarlo en local
1. Clona este repositorio.
2. Configura tu `GEMINI_API_KEY` en las variables de entorno de tu IDE.
3. Ejecuta la aplicación Spring Boot desde la clase `AtsApiApplication`.
4. (Opcional) En la carpeta `/postman` encontrarás la colección para importar, aunque recomendamos usar Swagger.

🔗 **[Swagger UI - Interactiva](http://localhost:8080/swagger-ui/index.h

## 📡 Endpoints Principales
**Endpoints Clave:**
* `POST /api/cvs/upload` - Sube un archivo PDF y extrae su texto en memoria.
* `POST /api/postulaciones` - Conecta al candidato con la oferta y dispara el análisis de la IA de Google.
* `GET /api/postulaciones/empresa/ofertas/{ofertaId}/candidatos` - Filtro avanzado de RRHH para obtener candidatos que superen cierta nota de compatibilidad.
