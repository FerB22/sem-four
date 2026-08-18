package com.example.semfour.data.local

import com.example.semfour.data.local.entity.DailyPlanTaskEntity

/**
 * Catálogo maestro de las 16 semanas del Cronograma Diario de Estudio de SemFour.
 * Contiene todas las asignaciones operativas de Lunes a Viernes con sus respectivos cuadernos .ipynb.
 */
object StudyPlanCatalog {

    fun generateAllTasks(): List<DailyPlanTaskEntity> {
        val tasks = mutableListOf<DailyPlanTaskEntity>()

        val rawSchedule = listOf(
            // SEMANA 01
            WeekData(
                week = 1,
                lunBD = "Semana_01_Bloques_Anonimos_RECORD_PLSQL.ipynb",
                lunPOO = "Semana_01_Paradigma_POO_Sintaxis_Java_Estructuras_Control.ipynb",
                marEtica = "Semana_01_Dignidad_Humana_Sentido_Etico_Trabajo.ipynb",
                marFS = "Semana_01_HTML5_Semantico_Accesibilidad_SEO.ipynb",
                marEst = "Semana_01_Introduccion_Python_Pandas.ipynb",
                miePOO = "Semana_01_Paradigma_POO_Sintaxis_Java_Estructuras_Control.ipynb",
                mieFS = "Semana_01_HTML5_Semantico_Accesibilidad_SEO.ipynb",
                jueEst = "Semana_01_Introduccion_Python_Pandas.ipynb",
                jueMovil = "Semana_01_Ecosistema_Movil_Android_Studio_Setup.ipynb"
            ),
            // SEMANA 02
            WeekData(
                week = 2,
                lunBD = "Semana_02_Colecciones_VARRAY_Tablas_Anidadas.ipynb",
                lunPOO = "Semana_02_Clases_Objetos_Constructores_Abstraccion.ipynb",
                marEtica = "Semana_02_Actos_Humanos_Libertad_Responsabilidad_Moral.ipynb",
                marFS = "Semana_02_CSS3_Avanzado_Flexbox_Grid.ipynb",
                marEst = "Semana_02_Conceptos_Basicos_Poblacion_Muestra_Variables.ipynb",
                miePOO = "Semana_02_Clases_Objetos_Constructores_Abstraccion.ipynb",
                mieFS = "Semana_02_CSS3_Avanzado_Flexbox_Grid.ipynb",
                jueEst = "Semana_02_Conceptos_Basicos_Poblacion_Muestra_Variables.ipynb",
                jueMovil = "Semana_02_Fundamentos_Kotlin_Null_Safety_Control_Flujo.ipynb"
            ),
            // SEMANA 03
            WeekData(
                week = 3,
                lunBD = "Semana_03_Cursores_Explicitos_Parametrizados.ipynb",
                lunPOO = "Semana_03_Encapsulamiento_Modificadores_Getters_Setters.ipynb",
                marEtica = "Semana_03_Excelencia_Profesional_Bien_Comun_Virtudes.ipynb",
                marFS = "Semana_03_Diseno_Responsivo_Media_Queries_Variables.ipynb",
                marEst = "Semana_03_Tablas_Frecuencia_Variables_Cualitativas_Discretas.ipynb",
                miePOO = "Semana_03_Encapsulamiento_Modificadores_Getters_Setters.ipynb",
                mieFS = "Semana_03_Diseno_Responsivo_Media_Queries_Variables.ipynb",
                jueEst = "Semana_03_Tablas_Frecuencia_Variables_Cualitativas_Discretas.ipynb",
                jueMovil = "Semana_03_Colecciones_Funciones_Lambdas_Kotlin.ipynb"
            ),
            // SEMANA 04
            WeekData(
                week = 4,
                lunBD = "Semana_04_Ciclos_Anidados_Transacciones_Procesamiento_Lotes.ipynb",
                lunPOO = "Semana_04_Herencia_Polimorfismo_Clases_Abstractas_Interfaces.ipynb",
                marEtica = "Semana_04_Dilemas_Eticos_Tecnologia_Privacidad_Propiedad.ipynb",
                marFS = "Semana_04_JavaScript_ES6_DOM_Manipulacion.ipynb",
                marEst = "Semana_04_Tablas_Frecuencia_Variables_Continuas_Intervalos.ipynb",
                miePOO = "Semana_04_Herencia_Polimorfismo_Clases_Abstractas_Interfaces.ipynb",
                mieFS = "Semana_04_JavaScript_ES6_DOM_Manipulacion.ipynb",
                jueEst = "Semana_04_Tablas_Frecuencia_Variables_Continuas_Intervalos.ipynb",
                jueMovil = "Semana_04_POO_Kotlin_Data_Classes_Herencia_Interfaces.ipynb"
            ),
            // SEMANA 05
            WeekData(
                week = 5,
                lunBD = "Semana_05_Manejo_Excepciones_Predefinidas_Usuario.ipynb",
                lunPOO = "Semana_05_Colecciones_ArrayList_HashMap_Manejo_Excepciones.ipynb",
                marEtica = "Semana_05_Metodologia_Resolucion_Dilemas_Caso_Integrador.ipynb",
                marFS = "Semana_05_Asincronia_Promises_Async_Await_Fetch.ipynb",
                marEst = "Semana_05_Visualizacion_Graficos_Matplotlib_Seaborn.ipynb",
                miePOO = "Semana_05_Colecciones_ArrayList_HashMap_Manejo_Excepciones.ipynb",
                mieFS = "Semana_05_Asincronia_Promises_Async_Await_Fetch.ipynb",
                jueEst = "Semana_05_Visualizacion_Graficos_Matplotlib_Seaborn.ipynb",
                jueMovil = "Semana_05_Corrutinas_Sintaxis_Avanzada_Primer_App.ipynb"
            ),
            // SEMANA 06
            WeekData(
                week = 6,
                lunBD = "Semana_06_Procedimientos_Almacenados_IN_OUT.ipynb",
                lunPOO = "Semana_06_Introduccion_Maven_Configuracion_JavaFX.ipynb",
                marEtica = "Semana_06_Cultura_Organizacional_Respeto_Equidad.ipynb",
                marFS = "Semana_06_Ecosistema_React_Vite_React_Bootstrap.ipynb",
                marEst = "Semana_06_Medidas_Tendencia_Central.ipynb",
                miePOO = "Semana_06_Introduccion_Maven_Configuracion_JavaFX.ipynb",
                mieFS = "Semana_06_Ecosistema_React_Vite_React_Bootstrap.ipynb",
                jueEst = "Semana_06_Medidas_Tendencia_Central.ipynb",
                jueMovil = "Semana_06_Arquitectura_MVVM_Estructura_Proyecto.ipynb"
            ),
            // SEMANA 07
            WeekData(
                week = 7,
                lunBD = "Semana_07_Funciones_Almacenadas_Retorno_SQL.ipynb",
                lunPOO = "Semana_07_Escenas_Layouts_Controles_JavaFX.ipynb",
                marEtica = "Semana_07_Codigos_Deontologicos_ACM_IEEE_Informatica.ipynb",
                marFS = "Semana_07_Componentes_Props_Renderizado_Condicional.ipynb",
                marEst = "Semana_07_Medidas_Posicion_Cuartiles_Percentiles_Boxplots.ipynb",
                miePOO = "Semana_07_Escenas_Layouts_Controles_JavaFX.ipynb",
                mieFS = "Semana_07_Componentes_Props_Renderizado_Condicional.ipynb",
                jueEst = "Semana_07_Medidas_Posicion_Cuartiles_Percentiles_Boxplots.ipynb",
                jueMovil = "Semana_07_Jetpack_Compose_Composables_Layouts_Modifiers.ipynb"
            ),
            // SEMANA 08
            WeekData(
                week = 8,
                lunBD = "Semana_08_Packages_PLSQL_Especificacion_Cuerpo.ipynb",
                lunPOO = "Semana_08_Patron_MVC_JavaFX_FXML_SceneBuilder.ipynb",
                marEtica = "Semana_08_Conflictos_Interes_Confidencialidad_NDA.ipynb",
                marFS = "Semana_08_Hooks_useState_useEffect_Ciclo_Vida.ipynb",
                marEst = "Semana_08_Medidas_Dispersion_Varianza_Desviacion_CV.ipynb",
                miePOO = "Semana_08_Patron_MVC_JavaFX_FXML_SceneBuilder.ipynb",
                mieFS = "Semana_08_Hooks_useState_useEffect_Ciclo_Vida.ipynb",
                jueEst = "Semana_08_Medidas_Dispersion_Varianza_Desviacion_CV.ipynb",
                jueMovil = "Semana_08_Listas_LazyColumn_Material3_Adaptabilidad.ipynb"
            ),
            // SEMANA 09
            WeekData(
                week = 9,
                lunBD = "Semana_09_Triggers_Nivel_Sentencia_Auditoria.ipynb",
                lunPOO = "Semana_09_TableView_ObservableList_Binding.ipynb",
                marEtica = "Semana_09_Responsabilidad_Social_Empresarial_Green_IT.ipynb",
                marFS = "Semana_09_Formularios_Controlados_Validaciones.ipynb",
                marEst = "Semana_09_Analisis_Bivariado_Dispersion_Correlacion_Pearson.ipynb",
                miePOO = "Semana_09_TableView_ObservableList_Binding.ipynb",
                mieFS = "Semana_09_Formularios_Controlados_Validaciones.ipynb",
                jueEst = "Semana_09_Analisis_Bivariado_Dispersion_Correlacion_Pearson.ipynb",
                jueMovil = "Semana_09_Navegacion_Jetpack_Compose_NavController.ipynb"
            ),
            // SEMANA 10
            WeekData(
                week = 10,
                lunBD = "Semana_10_Triggers_Nivel_Fila_NEW_OLD.ipynb",
                lunPOO = "Semana_10_Navegacion_Vistas_Validacion_Formularios.ipynb",
                marEtica = "Semana_10_Etica_Inteligencia_Artificial_Sesgo_Algoritmico.ipynb",
                marFS = "Semana_10_React_Router_DOM_Consumo_APIs.ipynb",
                marEst = "Semana_10_Regresion_Lineal_Simple_Minimos_Cuadrados.ipynb",
                miePOO = "Semana_10_Navegacion_Vistas_Validacion_Formularios.ipynb",
                mieFS = "Semana_10_React_Router_DOM_Consumo_APIs.ipynb",
                jueEst = "Semana_10_Regresion_Lineal_Simple_Minimos_Cuadrados.ipynb",
                jueMovil = "Semana_10_Gestion_Estado_ViewModel_StateFlow_Formularios.ipynb"
            ),
            // SEMANA 11
            WeekData(
                week = 11,
                lunBD = "Semana_11_Automatizacion_Reglas_Negocio_Oracle_APEX.ipynb",
                lunPOO = "Semana_11_Persistencia_JSON_Gson_Capa_Repository.ipynb",
                marEtica = "Semana_11_Analisis_Casos_Organizacionales_Resolucion_Conflictos.ipynb",
                marFS = "Semana_11_Testing_Frontend_Unit_Tests_Cobertura.ipynb",
                marEst = "Semana_11_Interpretacion_Regresion_Predicciones_Residuales.ipynb",
                miePOO = "Semana_11_Persistencia_JSON_Gson_Capa_Repository.ipynb",
                mieFS = "Semana_11_Testing_Frontend_Unit_Tests_Cobertura.ipynb",
                jueEst = "Semana_11_Interpretacion_Regresion_Predicciones_Residuales.ipynb",
                jueMovil = "Semana_11_Persistencia_Room_DataStore_Recursos_Nativos.ipynb"
            ),
            // SEMANA 12
            WeekData(
                week = 12,
                lunBD = "Semana_12_Introduccion_NoSQL_MongoDB_Modelado.ipynb",
                lunPOO = "Semana_12_Arquitectura_JDBC_Driver_Conectividad.ipynb",
                marEtica = "Semana_12_Liderazgo_Etico_Toma_Decisiones_Presion.ipynb",
                marFS = "Semana_12_Arquitectura_REST_Backend_Spring_Express.ipynb",
                marEst = "Semana_12_Fundamentos_Probabilidad_Reglas_Aditiva_Multiplicativa.ipynb",
                miePOO = "Semana_12_Arquitectura_JDBC_Driver_Conectividad.ipynb",
                mieFS = "Semana_12_Arquitectura_REST_Backend_Spring_Express.ipynb",
                jueEst = "Semana_12_Fundamentos_Probabilidad_Reglas_Aditiva_Multiplicativa.ipynb",
                jueMovil = "Semana_12_Consumo_APIs_REST_Retrofit_JSON.ipynb"
            ),
            // SEMANA 13
            WeekData(
                week = 13,
                lunBD = "Semana_13_Modelado_Documentos_Embebidos_Referencias.ipynb",
                lunPOO = "Semana_13_Conexion_Java_BD_DriverManager_Statement.ipynb",
                marEtica = "Semana_13_Trabajo_Equipo_Equidad_Inclusion_TI.ipynb",
                marFS = "Semana_13_Controladores_Servicios_CRUD_Validaciones.ipynb",
                marEst = "Semana_13_Probabilidad_Condicional_Independencia_Bayes.ipynb",
                miePOO = "Semana_13_Conexion_Java_BD_DriverManager_Statement.ipynb",
                mieFS = "Semana_13_Controladores_Servicios_CRUD_Validaciones.ipynb",
                jueEst = "Semana_13_Probabilidad_Condicional_Independencia_Bayes.ipynb",
                jueMovil = "Semana_13_Manejo_Asincrono_Estados_Red_UI_State.ipynb"
            ),
            // SEMANA 14
            WeekData(
                week = 14,
                lunBD = "Semana_14_CRUD_MongoDB_Insercion_Lectura_Filtros.ipynb",
                lunPOO = "Semana_14_Sentencias_Parametrizadas_PreparedStatement.ipynb",
                marEtica = "Semana_14_Automatizacion_Futuro_Trabajo_Desconexion_Digital.ipynb",
                marFS = "Semana_14_Integracion_Fullstack_React_Backend_CORS.ipynb",
                marEst = "Semana_14_Distribucion_Binomial_Calculo_SciPy.ipynb",
                miePOO = "Semana_14_Sentencias_Parametrizadas_PreparedStatement.ipynb",
                mieFS = "Semana_14_Integracion_Fullstack_React_Backend_CORS.ipynb",
                jueEst = "Semana_14_Distribucion_Binomial_Calculo_SciPy.ipynb",
                jueMovil = "Semana_14_Testing_Movil_JUnit_Mockk_Compose_Rule.ipynb"
            ),
            // SEMANA 15
            WeekData(
                week = 15,
                lunBD = "Semana_15_Actualizacion_Eliminacion_Operadores_MongoDB.ipynb",
                lunPOO = "Semana_15_Implementacion_Capa_DAO_CRUD_Completo.ipynb",
                marEtica = "Semana_15_Proyecto_Etico_Profesional_Perfil_Egreso.ipynb",
                marFS = "Semana_15_Autenticacion_JWT_Seguridad_Rutas.ipynb",
                marEst = "Semana_15_Distribucion_Normal_Estandarizacion_Z.ipynb",
                miePOO = "Semana_15_Implementacion_Capa_DAO_CRUD_Completo.ipynb",
                mieFS = "Semana_15_Autenticacion_JWT_Seguridad_Rutas.ipynb",
                jueEst = "Semana_15_Distribucion_Normal_Estandarizacion_Z.ipynb",
                jueMovil = "Semana_15_Seguridad_Optimizacion_ProGuard_Rendimiento.ipynb"
            ),
            // SEMANA 16
            WeekData(
                week = 16,
                lunBD = "Semana_16_Aggregation_Pipeline_Indexacion_Arquitectura_Hibrida.ipynb",
                lunPOO = "Semana_16_Integracion_Final_Arquitectura_Capas_JavaFX_DAO.ipynb",
                marEtica = "Semana_16_Sintesis_Transversal_Preparacion_Evaluacion_Final.ipynb",
                marFS = "Semana_16_Despliegue_Cloud_AWS_Azure_Postman_Testing.ipynb",
                marEst = "Semana_16_Aplicaciones_Distribucion_Normal_Caso_Integrador.ipynb",
                miePOO = "Semana_16_Integracion_Final_Arquitectura_Capas_JavaFX_DAO.ipynb",
                mieFS = "Semana_16_Despliegue_Cloud_AWS_Azure_Postman_Testing.ipynb",
                jueEst = "Semana_16_Aplicaciones_Distribucion_Normal_Caso_Integrador.ipynb",
                jueMovil = "Semana_16_Generacion_APK_Firmado_Release_EFT.ipynb"
            )
        )

        for (w in rawSchedule) {
            val weekPadded = String.format("%02d", w.week)

            // LUNES
            tasks.add(
                DailyPlanTaskEntity(
                    id = "plan_w${weekPadded}_d1_bd",
                    weekNumber = w.week,
                    dayOfWeek = 1,
                    subjectId = "sub_bd",
                    taskType = "Completar cuaderno semanal",
                    notebookFile = w.lunBD
                )
            )
            tasks.add(
                DailyPlanTaskEntity(
                    id = "plan_w${weekPadded}_d1_poo",
                    weekNumber = w.week,
                    dayOfWeek = 1,
                    subjectId = "sub_poo",
                    taskType = "Teoría y Ejercicios Nivel 1 (Warm-up)",
                    notebookFile = w.lunPOO
                )
            )

            // MARTES
            tasks.add(
                DailyPlanTaskEntity(
                    id = "plan_w${weekPadded}_d2_etica",
                    weekNumber = w.week,
                    dayOfWeek = 2,
                    subjectId = "sub_etica",
                    taskType = "Completar cuaderno y casos prácticos",
                    notebookFile = w.marEtica
                )
            )
            tasks.add(
                DailyPlanTaskEntity(
                    id = "plan_w${weekPadded}_d2_fs",
                    weekNumber = w.week,
                    dayOfWeek = 2,
                    subjectId = "sub_fullstack",
                    taskType = "Teoría y Primer Bloque de Código",
                    notebookFile = w.marFS
                )
            )
            tasks.add(
                DailyPlanTaskEntity(
                    id = "plan_w${weekPadded}_d2_est",
                    weekNumber = w.week,
                    dayOfWeek = 2,
                    subjectId = "sub_estadistica",
                    taskType = "Teoría y Métodos en Pandas",
                    notebookFile = w.marEst
                )
            )

            // MIÉRCOLES
            tasks.add(
                DailyPlanTaskEntity(
                    id = "plan_w${weekPadded}_d3_poo",
                    weekNumber = w.week,
                    dayOfWeek = 3,
                    subjectId = "sub_poo",
                    taskType = "Ejercicios Nivel 2 y 3 (Desafío de prueba)",
                    notebookFile = w.miePOO
                )
            )
            tasks.add(
                DailyPlanTaskEntity(
                    id = "plan_w${weekPadded}_d3_fs",
                    weekNumber = w.week,
                    dayOfWeek = 3,
                    subjectId = "sub_fullstack",
                    taskType = "Ejercicios Prácticos y Retos",
                    notebookFile = w.mieFS
                )
            )

            // JUEVES
            tasks.add(
                DailyPlanTaskEntity(
                    id = "plan_w${weekPadded}_d4_est",
                    weekNumber = w.week,
                    dayOfWeek = 4,
                    subjectId = "sub_estadistica",
                    taskType = "Práctica y Consolidación de Gráficos",
                    notebookFile = w.jueEst
                )
            )
            tasks.add(
                DailyPlanTaskEntity(
                    id = "plan_w${weekPadded}_d4_movil",
                    weekNumber = w.week,
                    dayOfWeek = 4,
                    subjectId = "sub_movil",
                    taskType = "Completar cuaderno Kotlin / Compose",
                    notebookFile = w.jueMovil
                )
            )

            // VIERNES
            tasks.add(
                DailyPlanTaskEntity(
                    id = "plan_w${weekPadded}_d5_cons",
                    weekNumber = w.week,
                    dayOfWeek = 5,
                    subjectId = "sub_consolidacion",
                    taskType = "Consolidación y Cierre Semanal",
                    notebookFile = "Revisión de desafíos y pruebas unitarias de las 5 asignaturas"
                )
            )
        }

        return tasks
    }

    private data class WeekData(
        val week: Int,
        val lunBD: String,
        val lunPOO: String,
        val marEtica: String,
        val marFS: String,
        val marEst: String,
        val miePOO: String,
        val mieFS: String,
        val jueEst: String,
        val jueMovil: String
    )
}
