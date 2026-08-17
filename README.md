# 📱 SemFour (4.º Semestre - Analista Programador)

![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-purple?logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-M3-brightgreen?logo=android)
![Android](https://img.shields.io/badge/Android-8.0%2B%20(API%2026%2B)-green?logo=android)
![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%2B%20Clean-blue)
![Database](https://img.shields.io/badge/Database-Room%20(Offline--First)-orange)
![Google Drive](https://img.shields.io/badge/Backup-Google%20Drive%20API-yellow?logo=googledrive)

**SemFour** es una aplicación móvil nativa para Android diseñada como el centro integral de estudio, planificación inteligente y seguimiento académico para estudiantes de **Analista Programador (4.º Semestre)**.

Combina el algoritmo científico de repetición espaciada **SuperMemo-2 (SM-2)** con el horario semanal oficial de clases, control de evaluaciones, mapa de calor de hábitos de estudio estilo GitHub, widgets para la pantalla de inicio y respaldo seguro en **Google Drive**.

---

## ✨ Características Principales

### 🧠 1. Priorización Inteligente de Estudio (Algoritmo SM-2)
* **Score dinámico:** Prioriza automáticamente qué tema debes repasar según:
  * **50 % Repetición Espaciada (SM-2):** Intervalos óptimos de memoria a largo plazo.
  * **30 % Confianza Inversa:** Mayor peso a temas donde te sientes menos seguro (1-2 estrellas).
  * **20 % Proximidad de Evaluaciones:** Urgencia de certámenes y entregas cercanas.
* **Modalidades de Sesión:**
  * ⚡ **Micro (5 min):** Repasos rápidos entre clases.
  * 🍅 **Pomodoro (25 min):** Bloques de concentración profunda.
  * ⏱️ **Libre:** Tiempo personalizado con temporizador reactivo.

### 📅 2. Horario Semanal Oficial
* Muestra las clases de Lunes a Viernes con salas de laboratorio PC / proyectos y profesores asignados.
* Incluye asignaturas base:
  * **DSY1102:** Desarrollo Orientado a Objetos (Java / JavaFX / MVC)
  * **BDY1103:** Taller de Base de Datos (Oracle PL/SQL / APEX / MongoDB)
  * **DSY1105:** Desarrollo de Aplicaciones Móviles (Kotlin / Jetpack Compose)
  * **DSY1104:** Desarrollo Fullstack II (React / Vite / Microservicios)
  * **MAT4141:** Estadística Descriptiva (Python / Pandas / Probabilidad)
  * **EAY4730:** Ética para el Trabajo (Dilemas Morales / Privacidad)
  * **INU5100:** Inglés Intermedio 1 (Modular / Opcional)

### 📝 3. Gestor de Evaluaciones Interactivo
* Diálogo emergente en Jetpack Compose Material 3 con calendario estilizado en blanco puro y carbón profundo.
* Permite crear, modificar o marcar evaluaciones como *"Por definir"* y recalcular fechas restantes en tiempo real.

### 🔥 4. Hábitos y Mapa de Calor de 90 Días
* Visualización gráfica del historial de estudio de los últimos 3 meses estilo GitHub.
* Contador de racha diaria de fuego y minutos acumulados por semana.

### 🧩 5. Widgets de Pantalla de Inicio (Jetpack Glance)
1. **Widget de Tema Prioritario:** Muestra el tema más urgente y tu próxima clase con sala.
2. **Widget de Racha Diaria:** Muestra los días consecutivos de estudio y minutos de hoy.
3. **Widget de Horario Semanal:** Acceso directo a tu agenda de clases.

### ☁️ 6. Respaldo y Sincronización en Google Drive
* Autenticación con Google Credential Manager / Google Sign-In.
* Respaldo automático cifrado en tu carpeta oculta de Google Drive (App Data Folder) con arquitectura 100 % **Offline-First**.

---

## 🛠️ Stack Tecnológico

* **Lenguaje:** Kotlin 2.0.0
* **UI Toolkit:** Jetpack Compose con Material Design 3 (Blanco Puro y Slate Charcoal)
* **Arquitectura:** MVVM + Clean Architecture + Repository Pattern
* **Inyección de Dependencias:** Dagger Hilt
* **Persistencia Local:** Room Database + SQLite
* **Widgets:** Jetpack Glance AppWidget
* **Sincronización en la Nube:** Google Drive REST API v3 + Google Play Services Auth
* **Concurrencia:** Kotlin Coroutines & Flow (StateFlow / SharedFlow)

---

## 📥 Descarga e Instalación

1. Descarga el archivo `.apk` más reciente desde la sección de [**Releases**](https://github.com/FerB22/sem-four/releases).
2. En tu dispositivo Android, abre el archivo descargado.
3. Si el sistema lo solicita, autoriza la instalación de aplicaciones de origen desconocido.
4. ¡Abre **SemFour** y comienza a optimizar tu semestre!

---

## 💻 Compilación desde el Código Fuente

```bash
# 1. Clonar el repositorio
git clone https://github.com/FerB22/sem-four.git
cd sem-four

# 2. Compilar el APK de depuración
./gradlew assembleDebug

# El APK se generará en: app/build/outputs/apk/debug/app-debug.apk
```

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Consulta el archivo `LICENSE` para más detalles.
