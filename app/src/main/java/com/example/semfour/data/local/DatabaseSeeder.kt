package com.example.semfour.data.local

import com.example.semfour.data.local.dao.QuizQuestionDao
import com.example.semfour.data.local.dao.DailyPlanDao
import com.example.semfour.data.local.dao.EvaluationDao
import com.example.semfour.data.local.dao.ScheduleDao
import com.example.semfour.data.local.dao.SubjectDao
import com.example.semfour.data.local.dao.TopicDao
import com.example.semfour.data.local.entity.EvaluationEntity
import com.example.semfour.data.local.entity.ScheduleEntity
import com.example.semfour.data.local.entity.SubjectEntity
import com.example.semfour.data.local.entity.TopicEntity
import com.example.semfour.domain.algorithm.SM2Engine
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Inicializa y gestiona los temas y evaluaciones oficiales del 4.º Semestre (Duoc UC).
 * Soporta la activación/desactivación dinámica de las Experiencias 1, 2 y 3 y el Cronograma de 16 semanas.
 */
@Singleton
class DatabaseSeeder @Inject constructor(
    private val subjectDao: SubjectDao,
    private val topicDao: TopicDao,
    private val evaluationDao: EvaluationDao,
    private val scheduleDao: ScheduleDao,
    private val dailyPlanDao: DailyPlanDao,
    private val quizQuestionDao: QuizQuestionDao
) {
    companion object {
        const val UN_DIA_MS = 86_400_000L
    }

    suspend fun seedIfEmpty() {
        val ahora = System.currentTimeMillis()

        if (subjectDao.count() == 0) {
            val subjects = listOf(
                SubjectEntity(
                    id = "sub_poo",
                    nombre = "Desarrollo Orientado a Objetos",
                    codigo = "DSY1102",
                    icono = "code",
                    color = "#F89820"
                ),
                SubjectEntity(
                    id = "sub_bd",
                    nombre = "Taller de Base de Datos",
                    codigo = "BDY1103",
                    icono = "database",
                    color = "#E53935"
                ),
                SubjectEntity(
                    id = "sub_fullstack",
                    nombre = "Desarrollo Fullstack II",
                    codigo = "DSY1104",
                    icono = "web",
                    color = "#61DAFB"
                ),
                SubjectEntity(
                    id = "sub_estadistica",
                    nombre = "Estadística Descriptiva",
                    codigo = "MAT4141",
                    icono = "bar_chart",
                    color = "#8E24AA"
                ),
                SubjectEntity(
                    id = "sub_moviles",
                    nombre = "Desarrollo de Aplicaciones Móviles",
                    codigo = "DSY1105",
                    icono = "smartphone",
                    color = "#3DDC84"
                ),
                SubjectEntity(
                    id = "sub_etica",
                    nombre = "Ética para el Trabajo",
                    codigo = "EAY4730",
                    icono = "balance",
                    color = "#3949AB"
                )
            )
            subjectDao.insertSubjects(subjects)
        }

        // ── 2. Horario Semanal Oficial ────────────────────────────────────────
        if (scheduleDao.count() == 0) {
            val schedules = listOf(
                // Lunes (1)
                ScheduleEntity("sch_lunes_poo", "sub_poo", 1, "10:01", "12:10", "SALA 1208 LAB PC (38)", "David Andrés Azúa Ulloa"),
                ScheduleEntity("sch_lunes_bd", "sub_bd", 1, "13:41", "16:40", "SALA 1207 LAB PC (32)", "Gilda Orellana Guzmán"),

                // Martes (2)
                ScheduleEntity("sch_martes_etica", "sub_etica", 2, "12:11", "13:40", "SALA 1305 SALA PROYECTOS (30)", "Nicolás Matías Fuentes Valdebenito"),
                ScheduleEntity("sch_martes_fs", "sub_fullstack", 2, "13:41", "15:50", "SALA 1208 LAB PC (38)", "David Andrés Azúa Ulloa"),
                ScheduleEntity("sch_martes_est", "sub_estadistica", 2, "16:01", "17:20", "SALA 1207 LAB PC (32)", "Francisco Javier Saavedra Jara"),

                // Miércoles (3)
                ScheduleEntity("sch_miercoles_poo", "sub_poo", 3, "10:01", "11:20", "SALA 1208 LAB PC (38)", "David Andrés Azúa Ulloa"),
                ScheduleEntity("sch_miercoles_fs", "sub_fullstack", 3, "13:41", "15:50", "SALA 1208 LAB PC (38)", "David Andrés Azúa Ulloa"),

                // Jueves (4)
                ScheduleEntity("sch_jueves_est", "sub_estadistica", 4, "13:41", "15:10", "SALA 1207 LAB PC (32)", "Francisco Javier Saavedra Jara"),
                ScheduleEntity("sch_jueves_mov", "sub_moviles", 4, "15:11", "18:50", "SALA 1207 LAB PC (32)", "Gilda Orellana Guzmán")
            )
            scheduleDao.insertSchedule(schedules)
        }

        // ── 3. Experiencia 1 activa por defecto ──────────────────────────────
        if (topicDao.count() == 0) {
            topicDao.insertTopics(getExp1Topics(ahora))
        }

        // Sincronizar evaluaciones de Experiencia 1 con los datos reales e indeterminados
        evaluationDao.insertEvaluations(getExp1Evaluations(ahora))

        // Si inglés está habilitado, asegurar su horario de clases
        if (isEnglishEnabled()) {
            scheduleDao.insertSchedule(getEnglishSchedule())
        }

        // ── 4. Cronograma Diario de Estudio (16 Semanas) ────────────────────
        if (dailyPlanDao.getTaskCount() == 0) {
            dailyPlanDao.insertTasks(StudyPlanCatalog.generateAllTasks())
        }

        // ── 5. Banco Oficial de Preguntas de Estudio (Quiz / Active Recall) ──
        quizQuestionDao.insertQuestions(QuestionBankCatalog.getAllOfficialQuestions())
    }

    // ── MÉTODOS DE ACTIVACIÓN / DESACTIVACIÓN DINÁMICA ─────────────────────────

    suspend fun setExperienceEnabled(expNumber: Int, enabled: Boolean) {
        val ahora = System.currentTimeMillis()
        val englishActive = isEnglishEnabled()
        when (expNumber) {
            2 -> {
                val topicsExp2 = getExp2Topics(ahora).filter { englishActive || it.subjectId != "sub_ingles" }
                val evalsExp2 = getExp2Evaluations(ahora).filter { englishActive || it.subjectId != "sub_ingles" }
                if (enabled) {
                    topicDao.insertTopics(topicsExp2)
                    evaluationDao.insertEvaluations(evalsExp2)
                } else {
                    topicDao.deleteTopicsByIds(getExp2Topics(ahora).map { it.id })
                    evaluationDao.deleteEvaluationsByIds(getExp2Evaluations(ahora).map { it.id })
                }
            }
            3 -> {
                val topicsExp3 = getExp3Topics(ahora).filter { englishActive || it.subjectId != "sub_ingles" }
                val evalsExp3 = getExp3Evaluations(ahora).filter { englishActive || it.subjectId != "sub_ingles" }
                if (enabled) {
                    topicDao.insertTopics(topicsExp3)
                    evaluationDao.insertEvaluations(evalsExp3)
                } else {
                    topicDao.deleteTopicsByIds(getExp3Topics(ahora).map { it.id })
                    evaluationDao.deleteEvaluationsByIds(getExp3Evaluations(ahora).map { it.id })
                }
            }
        }
    }

    suspend fun isExperienceEnabled(expNumber: Int): Boolean {
        return when (expNumber) {
            1 -> true
            2 -> topicDao.getTopicById("top_mov_2_1") != null
            3 -> topicDao.getTopicById("top_mov_3_1") != null
            else -> false
        }
    }

    suspend fun isEnglishEnabled(): Boolean {
        return subjectDao.getSubjectById("sub_ingles") != null
    }

    suspend fun setEnglishEnabled(enabled: Boolean) {
        val ahora = System.currentTimeMillis()
        if (enabled) {
            val englishSubject = SubjectEntity(
                id = "sub_ingles",
                nombre = "Inglés Intermedio 1",
                codigo = "INU5100",
                icono = "language",
                color = "#00897B"
            )
            subjectDao.insertSubject(englishSubject)

            // Insertar horario oficial de clases de Inglés (Lunes y Miércoles 16:41 - 18:10)
            scheduleDao.insertSchedule(getEnglishSchedule())

            // Insertar temas y evaluaciones de Exp 1 de Inglés
            topicDao.insertTopics(getEnglishExp1Topics(ahora))
            evaluationDao.insertEvaluations(getEnglishExp1Evaluations(ahora))

            // Si Exp 2 está activa, insertar también sus temas
            if (isExperienceEnabled(2)) {
                topicDao.insertTopics(getEnglishExp2Topics(ahora))
                evaluationDao.insertEvaluations(getEnglishExp2Evaluations(ahora))
            }

            // Si Exp 3 está activa, insertar también sus temas
            if (isExperienceEnabled(3)) {
                topicDao.insertTopics(getEnglishExp3Topics(ahora))
                evaluationDao.insertEvaluations(getEnglishExp3Evaluations(ahora))
            }
        } else {
            // Eliminar horario, temas y evaluaciones de inglés
            scheduleDao.deleteScheduleForSubject("sub_ingles")
            val allEnglishTopicIds = (getEnglishExp1Topics(ahora) + getEnglishExp2Topics(ahora) + getEnglishExp3Topics(ahora)).map { it.id }
            val allEnglishEvalIds = (getEnglishExp1Evaluations(ahora) + getEnglishExp2Evaluations(ahora) + getEnglishExp3Evaluations(ahora)).map { it.id }
            topicDao.deleteTopicsByIds(allEnglishTopicIds)
            evaluationDao.deleteEvaluationsByIds(allEnglishEvalIds)
            subjectDao.deleteSubjectById("sub_ingles")
        }
    }

    fun getEnglishSchedule(): List<ScheduleEntity> = listOf(
        ScheduleEntity(
            id = "sch_lunes_ingles",
            subjectId = "sub_ingles",
            dayOfWeek = 1,
            startTime = "16:41",
            endTime = "18:10",
            room = "SALA 1211 SALA PROYECTOS (20)",
            professor = "Skarlet Andrea Fernandoy Fernandoy"
        ),
        ScheduleEntity(
            id = "sch_miercoles_ingles",
            subjectId = "sub_ingles",
            dayOfWeek = 3,
            startTime = "16:41",
            endTime = "18:10",
            room = "SALA 1211 SALA PROYECTOS (20)",
            professor = "Skarlet Andrea Fernandoy Fernandoy"
        )
    )

    private fun createTopic(
        id: String,
        subjectId: String,
        nombre: String,
        nivelConfianza: Int,
        ahora: Long,
        driveLinks: String = "[]"
    ): TopicEntity {
        val ef = SM2Engine.efFromNivelConfianza(nivelConfianza)
        return TopicEntity(
            id = id,
            subjectId = subjectId,
            nombre = nombre,
            nivelConfianza = nivelConfianza,
            factorFacilidad = ef,
            intervaloDias = 1,
            repeticiones = 0,
            tiempoEstudiadoAcumulado = 0,
            ultimoRepaso = null,
            proximoRepaso = ahora,
            driveLinksJson = driveLinks,
            updatedAt = ahora
        )
    }

    // ── EXPERIENCIA 1 ─────────────────────────────────────────────────────────

    fun getExp1Topics(ahora: Long): List<TopicEntity> = listOf(
        // Móviles
        createTopic("top_mov_1", "sub_moviles", "1.1 Ecosistema Móvil: Nativo vs Multiplataforma (Kotlin, Swift, Flutter, React Native)", 3, ahora),
        createTopic("top_mov_2", "sub_moviles", "1.2 Programación en Kotlin y sus Fundamentos (Variables, Tipos, Funciones)", 3, ahora),
        createTopic("top_mov_3", "sub_moviles", "1.3 Colecciones y Funciones de Orden Superior en Kotlin (List, Map, Filter, Lambdas)", 2, ahora),
        createTopic("top_mov_4", "sub_moviles", "1.4 POO, Clases y Control de Errores en Kotlin (Data Classes, Try-Catch, Null Safety)", 2, ahora),
        createTopic("top_mov_5", "sub_moviles", "1.5 Corrutinas y Programación Asíncrona Avanzada en Kotlin (Dispatchers, Flow)", 1, ahora),
        createTopic("top_mov_6", "sub_moviles", "1.6 Android Studio y Arquitectura de la Primera App (Activity Lifecycle, Compose)", 2, ahora),

        // POO
        createTopic("top_poo_1", "sub_poo", "1.1 Paradigma POO, Tipos de Datos y Operadores en Java", 3, ahora),
        createTopic("top_poo_2", "sub_poo", "1.2 Estructuras de Control, Métodos y Modularidad", 3, ahora),
        createTopic("top_poo_3", "sub_poo", "1.3 Clases, Objetos, Abstracción y Encapsulamiento (Getters/Setters)", 3, ahora),
        createTopic("top_poo_4", "sub_poo", "1.4 Herencia y Polimorfismo en Java", 2, ahora),
        createTopic("top_poo_5", "sub_poo", "1.5 Clases Abstractas e Interfaces", 2, ahora),
        createTopic("top_poo_6", "sub_poo", "1.6 Colecciones en Java y Manejo Robusto de Excepciones", 2, ahora),

        // Fullstack 2
        createTopic("top_fs_1", "sub_fullstack", "1.1 Explorando HTML5 Semántico (<section>, <nav>, <article>, formularios)", 4, ahora),
        createTopic("top_fs_2", "sub_fullstack", "1.2 CSS3, Box Model, Flexbox y Grid para Maquetación Adaptable", 3, ahora),
        createTopic("top_fs_3", "sub_fullstack", "1.3 Buenas Prácticas de Estilos y Frameworks CSS (Bootstrap / CDN)", 3, ahora),
        createTopic("top_fs_4", "sub_fullstack", "1.4 JavaScript Moderno: Lógica, Manipulación DOM y Eventos", 3, ahora),
        createTopic("top_fs_5", "sub_fullstack", "1.5 Validación de Formularios Web y Plugins Interactivos (DataTables)", 2, ahora),
        createTopic("top_fs_6", "sub_fullstack", "1.6 Control de Versiones con Git y GitHub para Proyectos Web", 3, ahora),

        // BD
        createTopic("top_bd_1", "sub_bd", "1.1 Estructuras Compuestas: RECORD y VARRAY en PL/SQL Oracle", 2, ahora),
        createTopic("top_bd_2", "sub_bd", "1.2 Cursores Complejos con Parámetros y Ciclos Anidados (FOR cursor IN)", 2, ahora),
        createTopic("top_bd_3", "sub_bd", "1.3 Manejo de Excepciones Predefinidas y Definidas por el Usuario (RAISE)", 2, ahora),
        createTopic("top_bd_4", "sub_bd", "1.4 Procedimientos Almacenados y Funciones en Oracle PL/SQL", 2, ahora),
        createTopic("top_bd_5", "sub_bd", "1.5 Paquetes (Packages) y Triggers de Base de Datos (:OLD, :NEW)", 1, ahora),

        // Estadística
        createTopic("top_est_1", "sub_estadistica", "1.1 Python y Pandas para Estadística (DataFrames, Series, head, shape, info, astype)", 3, ahora),
        createTopic("top_est_2", "sub_estadistica", "1.2 Clasificación de Variables y Tablas de Frecuencia (fi, Fi, hi, Hi)", 3, ahora),
        createTopic("top_est_3", "sub_estadistica", "1.3 Medidas de Tendencia Central (Media, Mediana, Moda) y Posición (Cuartiles)", 3, ahora),
        createTopic("top_est_4", "sub_estadistica", "1.4 Medidas de Dispersión (Varianza, Desviación Estándar, Coeficiente Variación)", 2, ahora),
        createTopic("top_est_5", "sub_estadistica", "1.5 Representación Gráfica e Interpretación (Histogramas, Boxplots, Barras)", 3, ahora),

        // Ética
        createTopic("top_et_1", "sub_etica", "1.1 Dimensión Humana y Moral de la Profesión Informática", 4, ahora),
        createTopic("top_et_2", "sub_etica", "1.2 Análisis y Resolución de Dilemas Éticos Profesionales (Dilemas 1, 2 y 3)", 4, ahora),
        createTopic("top_et_3", "sub_etica", "1.3 Privacidad de Datos, Responsabilidad Social y Códigos Deontológicos", 4, ahora)
    )

    private fun parseDate(dateStr: String): Long {
        return try {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
            sdf.parse(dateStr)?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }

    fun getExp1Evaluations(ahora: Long): List<EvaluationEntity> = listOf(
        EvaluationEntity("eval_poo_1", "sub_poo", "Evaluación Parcial 1: Fundamentos POO en Java", parseDate("2026-09-14 10:01"), 0.25f, "CERTAMEN", false),
        EvaluationEntity("eval_est_1", "sub_estadistica", "Exposición: Análisis Descriptivo con Python y Pandas", parseDate("2026-09-22 16:01"), 0.25f, "ENTREGA", false),
        EvaluationEntity("eval_mov_1", "sub_moviles", "Evaluación Parcial 1: Programación Móvil Kotlin", 0L, 0.30f, "ENTREGA", false),
        EvaluationEntity("eval_bd_1", "sub_bd", "Evaluación 1: Programación PL/SQL Avanzada", 0L, 0.25f, "CERTAMEN", false),
        EvaluationEntity("eval_fs_1", "sub_fullstack", "Entrega 1: Maquetación Web HTML5/CSS3/JS Adaptativa", 0L, 0.20f, "ENTREGA", false),
        EvaluationEntity("eval_et_1", "sub_etica", "Caso de Estudio: Dilemas Éticos en la Profesión", 0L, 0.20f, "ENTREGA", false)
    )

    // ── EXPERIENCIA 2 ─────────────────────────────────────────────────────────

    fun getExp2Topics(ahora: Long): List<TopicEntity> = listOf(
        // Móviles (EA2)
        createTopic("top_mov_2_1", "sub_moviles", "2.1 Configuración de Proyecto Móvil con MVVM y Herramientas Colaborativas", 3, ahora),
        createTopic("top_mov_2_2", "sub_moviles", "2.2 Construcción Visual de Pantalla Base con Jetpack Compose y Modifiers", 3, ahora),
        createTopic("top_mov_2_3", "sub_moviles", "2.3 Adaptabilidad de Diseño (Screen Sizes, WindowSizeClass, Orientación)", 2, ahora),
        createTopic("top_mov_2_4", "sub_moviles", "2.4 Navegación y Estructura Visual con NavHost, NavigationBar y Scaffold", 3, ahora),
        createTopic("top_mov_2_5", "sub_moviles", "2.5 Formularios, Validaciones y Paso de Información entre Pantallas", 2, ahora),
        createTopic("top_mov_2_6", "sub_moviles", "2.6 Gestión del Estado, State Hoisting, remember y Animaciones en Compose", 2, ahora),
        createTopic("top_mov_2_7", "sub_moviles", "2.7 Integración de Recursos Nativos: Cámara y Permisos en Android", 2, ahora),
        createTopic("top_mov_2_8", "sub_moviles", "2.8 Persistencia Local de Datos (Room Database y DataStore Preferences)", 2, ahora),

        // POO (EA2)
        createTopic("top_poo_2_1", "sub_poo", "2.1 Introducción a Maven, Gestión de Dependencias y Estructura pom.xml", 3, ahora),
        createTopic("top_poo_2_2", "sub_poo", "2.2 Configuración de JavaFX, Escenas, Stage y Ciclo de Vida de UI", 3, ahora),
        createTopic("top_poo_2_3", "sub_poo", "2.3 Implementación del Patrón Arquitectónico MVC en JavaFX", 3, ahora),
        createTopic("top_poo_2_4", "sub_poo", "2.4 Componentes Visuales Avanzados: TableView y ObservableList", 2, ahora),
        createTopic("top_poo_2_5", "sub_poo", "2.5 Navegación entre Vistas, FXML y Validación de Formularios", 2, ahora),
        createTopic("top_poo_2_6", "sub_poo", "2.6 Persistencia de Datos con JSON, Jackson y Capa Repository/DAO", 2, ahora),

        // Fullstack 2 (EA2)
        createTopic("top_fs_2_1", "sub_fullstack", "2.1 Configuración de Proyectos Frontend con React, Vite y Bootstrap", 3, ahora),
        createTopic("top_fs_2_2", "sub_fullstack", "2.2 Diseño Web Responsivo y Despliegue en Servidores Cloud (AWS EC2)", 3, ahora),
        createTopic("top_fs_2_3", "sub_fullstack", "2.3 Configuración de Pruebas Unitarias Frontend con Jasmine y Karma", 2, ahora),
        createTopic("top_fs_2_4", "sub_fullstack", "2.4 Creación de Pruebas Unitarias de Componentes y Reportes de Cobertura", 2, ahora),

        // BD (EA2)
        createTopic("top_bd_2_1", "sub_bd", "2.1 Procedimientos y Funciones Almacenadas en Oracle PL/SQL", 2, ahora),
        createTopic("top_bd_2_2", "sub_bd", "2.2 Construcción, Especificación e Integración de Packages PL/SQL", 2, ahora),
        createTopic("top_bd_2_3", "sub_bd", "2.3 Triggers a Nivel de Sentencia y Triggers a Nivel de Fila (:OLD, :NEW)", 2, ahora),
        createTopic("top_bd_2_4", "sub_bd", "2.4 Creación y Publicación de Aplicaciones con Oracle APEX", 3, ahora),

        // Estadística (EA2)
        createTopic("top_est_2_1", "sub_estadistica", "2.1 Modelos de Regresión Lineal Simple y Coeficiente de Determinación R²", 2, ahora),
        createTopic("top_est_2_2", "sub_estadistica", "2.2 Teoría de Probabilidades, Eventos y Probabilidad Condicional", 2, ahora),
        createTopic("top_est_2_3", "sub_estadistica", "2.3 Distribución de Probabilidad Normal, Estandarización Z y Campana de Gauss", 2, ahora),

        // Ética (EA2)
        createTopic("top_et_2_1", "sub_etica", "2.1 Ética en el Desarrollo de Software, IA y Manejo de Datos Sensibles", 4, ahora),
        createTopic("top_et_2_2", "sub_etica", "2.2 Análisis de Dilemas Éticos Organizacionales (Dilemas 4, 5 y 6)", 4, ahora),
        createTopic("top_et_2_3", "sub_etica", "2.3 Derechos Laborales, Propiedad Intelectual y Licenciamiento", 4, ahora)
    )

    fun getExp2Evaluations(ahora: Long): List<EvaluationEntity> = listOf(
        EvaluationEntity("eval_poo_2", "sub_poo", "Evaluación Parcial 2: JavaFX, MVC y Persistencia JSON", parseDate("2026-10-26 10:01"), 0.35f, "CERTAMEN", false),
        EvaluationEntity("eval_mov_2", "sub_moviles", "Evaluación 2: App Multi-pantalla con Compose y Room", 0L, 0.35f, "ENTREGA", false),
        EvaluationEntity("eval_bd_2", "sub_bd", "Evaluación 2: Packages, Triggers y Oracle APEX", 0L, 0.35f, "CERTAMEN", false),
        EvaluationEntity("eval_est_2", "sub_estadistica", "Certamen 2: Regresión Lineal y Probabilidad Normal", 0L, 0.35f, "CERTAMEN", false),
        EvaluationEntity("eval_fs_2", "sub_fullstack", "Entrega 2: Frontend React Responsivo con Test Unitarios", 0L, 0.35f, "ENTREGA", false),
        EvaluationEntity("eval_et_2", "sub_etica", "Caso 2: Análisis de Dilemas Organizacionales", 0L, 0.35f, "ENTREGA", false)
    )

    // ── EXPERIENCIA 3 ─────────────────────────────────────────────────────────

    fun getExp3Topics(ahora: Long): List<TopicEntity> = listOf(
        // Móviles (EA3)
        createTopic("top_mov_3_1", "sub_moviles", "3.1 Consumo de API REST con Retrofit/Ktor y Serialización JSON", 2, ahora),
        createTopic("top_mov_3_2", "sub_moviles", "3.2 Testing en Android: Pruebas Unitarias y Pruebas de UI con Compose", 2, ahora),
        createTopic("top_mov_3_3", "sub_moviles", "3.3 Generación de APK y AAB Firmado para Publicación en Google Play", 3, ahora),

        // POO (EA3)
        createTopic("top_poo_3_1", "sub_poo", "3.1 Conectividad JDBC a Base de Datos Relacional y Sentencias Parametrizadas", 2, ahora),
        createTopic("top_poo_3_2", "sub_poo", "3.2 Refactorización de la Capa DAO e Integración Completa con la UI JavaFX", 2, ahora),

        // Fullstack 2 (EA3)
        createTopic("top_fs_3_1", "sub_fullstack", "3.1 Arquitectura de Microservicios Backend con Spring Boot / Node.js", 2, ahora),
        createTopic("top_fs_3_2", "sub_fullstack", "3.2 Comunicación RESTful, Operaciones CRUD y Conexión Frontend React", 2, ahora),
        createTopic("top_fs_3_3", "sub_fullstack", "3.3 Despliegue de API RESTful en Cloud (AWS EC2 / Azure) y Pruebas con Postman", 3, ahora),

        // BD (EA3)
        createTopic("top_bd_3_1", "sub_bd", "3.1 Comparativa: Bases de Datos Relacionales (SQL) vs No Relacionales (NoSQL)", 3, ahora),
        createTopic("top_bd_3_2", "sub_bd", "3.2 Modelado de Documentos e Implementación en MongoDB", 2, ahora),
        createTopic("top_bd_3_3", "sub_bd", "3.3 Manipulación de Datos en MongoDB (CRUD y Consultas de Agregación)", 2, ahora),

        // Ética (EA3)
        createTopic("top_et_3_1", "sub_etica", "3.1 Ética en la Transformación Digital y Automatización", 4, ahora),
        createTopic("top_et_3_2", "sub_etica", "3.2 Resolución Integral de Dilemas Éticos Profesionales (Dilemas 7 y 8)", 4, ahora)
    )

    fun getExp3Evaluations(ahora: Long): List<EvaluationEntity> = listOf(
        EvaluationEntity("eval_poo_3", "sub_poo", "Evaluación Parcial 3: Sistema JavaFX con Conectividad JDBC", parseDate("2026-11-23 10:01"), 0.40f, "CERTAMEN", false),
        EvaluationEntity("eval_bd_3", "sub_bd", "Evaluación 3: NoSQL con MongoDB y Modelado Documental", 0L, 0.40f, "CERTAMEN", false),
        EvaluationEntity("eval_mov_3", "sub_moviles", "Evaluación 3: App Móvil Final con Consumo REST y Testing", 0L, 0.40f, "ENTREGA", false),
        EvaluationEntity("eval_fs_3", "sub_fullstack", "Entrega 3: Proyecto Fullstack Integrado con API Cloud", 0L, 0.45f, "ENTREGA", false),
        EvaluationEntity("eval_et_3", "sub_etica", "Evaluación Final: Proyecto Deontológico Profesional", 0L, 0.45f, "ENTREGA", false)
    )

    // ── INGLÉS INTERMEDIO 1 (OPCIONAL) ────────────────────────────────────────

    fun getEnglishExp1Topics(ahora: Long): List<TopicEntity> = listOf(
        createTopic("top_ing_1_1", "sub_ingles", "1.1 Grammar in Context: Present Perfect vs Simple Past in Tech Workplaces", 3, ahora),
        createTopic("top_ing_1_2", "sub_ingles", "1.2 Tech Vocabulary: Hardware, Software Systems & Troubleshooting", 3, ahora),
        createTopic("top_ing_1_3", "sub_ingles", "1.3 Professional Communication: Writing Technical Emails & Progress Reports", 3, ahora),
        createTopic("top_ing_1_4", "sub_ingles", "1.4 Describing Processes & Systems: Passive Voice in Technical Descriptions", 2, ahora),
        createTopic("top_ing_1_5", "sub_ingles", "1.5 Fluency & Pronunciation: IT Team Conversations & Daily Standups", 3, ahora)
    )

    fun getEnglishExp1Evaluations(ahora: Long): List<EvaluationEntity> = listOf(
        EvaluationEntity("eval_ing_1", "sub_ingles", "Evaluación 1: Oral Communication & Technical Vocabulary", 0L, 0.25f, "ENTREGA", false)
    )

    fun getEnglishExp2Topics(ahora: Long): List<TopicEntity> = listOf(
        createTopic("top_ing_2_1", "sub_ingles", "2.1 Conditional Sentences (First & Second Conditionals) in Software Logic", 3, ahora),
        createTopic("top_ing_2_2", "sub_ingles", "2.2 Modal Verbs for Advice, Obligation & Probability in IT Environments", 3, ahora),
        createTopic("top_ing_2_3", "sub_ingles", "2.3 Reading & Analysis of Technical Documentation and API Specifications", 2, ahora),
        createTopic("top_ing_2_4", "sub_ingles", "2.4 Explaining Technical Solutions & Troubleshooting to Clients", 2, ahora)
    )

    fun getEnglishExp2Evaluations(ahora: Long): List<EvaluationEntity> = listOf(
        EvaluationEntity("eval_ing_2", "sub_ingles", "Evaluación 2: Technical Reading Comprehension & Writing", 0L, 0.35f, "CERTAMEN", false)
    )

    fun getEnglishExp3Topics(ahora: Long): List<TopicEntity> = listOf(
        createTopic("top_ing_3_1", "sub_ingles", "3.1 Job Interviews in Tech: Describing Skills, Achievements & Portfolio", 3, ahora),
        createTopic("top_ing_3_2", "sub_ingles", "3.2 Technical Presentations: Structuring Demos, Signposting & Q&A", 3, ahora),
        createTopic("top_ing_3_3", "sub_ingles", "3.3 Collaboration in Agile/Scrum Teams: Sprint Reviews & Retrospectives", 3, ahora)
    )

    fun getEnglishExp3Evaluations(ahora: Long): List<EvaluationEntity> = listOf(
        EvaluationEntity("eval_ing_3", "sub_ingles", "Evaluación Final: Tech Presentation & Professional Interview", 0L, 0.40f, "ENTREGA", false)
    )
}
