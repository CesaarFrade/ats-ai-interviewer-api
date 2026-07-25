# 🚀 Smart ATS & AI Interviewer API

Un Sistema de Seguimiento de Candidatos (ATS) potenciado por Inteligencia Artificial. Este proyecto backend automatiza el primer filtro de contratación: analiza currículums (PDFs), hace un "match" con ofertas de trabajo y realiza entrevistas técnicas autónomas mediante agentes de IA.

## 🎯 Por qué este proyecto
Desarrollado para demostrar habilidades en la integración de flujos de negocio tradicionales con capacidades de IA generativa, aplicando buenas prácticas de desarrollo y arquitectura.

## ⚙️ Stack Tecnológico
* **Core:** Java 21, Spring Boot 3
* **Base de Datos:** PostgreSQL
* **IA / Agentes:** Spring AI / LangChain4j (LLM: OpenAI/Claude)
* **Arquitectura:** Arquitectura Hexagonal (Puertos y Adaptadores)
* **Infraestructura:** Docker, Docker Compose

## 🏗️ Arquitectura y Flujo
*(Aquí añadiremos un diagrama usando Mermaid.js cuando tengamos claro el diseño).*

1. El candidato sube su CV (PDF).
2. El sistema extrae texto y evalúa el perfil contra la oferta.
3. Si el *match* es > 70%, un Agente IA entrevista al candidato vía chat.

## 🚀 Cómo ejecutarlo en local
*(Aquí pondremos los comandos de Docker y Maven/Gradle para que cualquiera pueda correr tu app con 2 clicks).*

## 📡 Endpoints Principales
*(Aquí listaremos 3 o 4 endpoints clave o un enlace a Swagger cuando lo configuremos).*
