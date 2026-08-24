package com.example.semfour.data.local

import com.example.semfour.data.local.entity.QuizQuestionEntity

/**
 * Catálogo oficial de preguntas de selección múltiple (Active Recall / Quiz)
 * para los temas de las 6 asignaturas del 4.º Semestre.
 */
object QuestionBankCatalog {

    fun getAllOfficialQuestions(): List<QuizQuestionEntity> = listOf(
        // ══════════════════════════════════════════════════════════════════════════
        // DESARROLLO DE APLICACIONES MÓVILES (DSY1105)
        // ══════════════════════════════════════════════════════════════════════════
        // top_mov_1: Ecosistema Móvil
        QuizQuestionEntity(
            id = "q_mov_1_1",
            topicId = "top_mov_1",
            question = "¿Cuál es la principal ventaja de Kotlin Multiplatform (KMP) frente a Flutter o React Native?",
            optionA = "Permite compartir la lógica de negocio en Kotlin compilando a código nativo sin forzar un motor de renderizado propio",
            optionB = "Obliga a compilar todo a JavaScript para ejecutar en un WebView",
            optionC = "Solo funciona en dispositivos Android y no en iOS",
            optionD = "Reemplaza completamente a Swift en todos los proyectos de Apple",
            correctOptionIndex = 0,
            explanation = "KMP permite compartir lógica (red, base de datos, algoritmos) en Kotlin nativo mientras mantienes la UI nativa (SwiftUI en iOS, Compose en Android)."
        ),
        QuizQuestionEntity(
            id = "q_mov_1_2",
            topicId = "top_mov_1",
            question = "¿Qué lenguaje es recomendado oficialmente por Google como prioritario (Kotlin-First) para el desarrollo nativo en Android?",
            optionA = "Java",
            optionB = "Kotlin",
            optionC = "Dart",
            optionD = "C++",
            correctOptionIndex = 1,
            explanation = "Google declaró a Kotlin como lenguaje oficial prioritario para Android en el Google I/O 2019."
        ),

        // top_mov_2: Fundamentos de Kotlin
        QuizQuestionEntity(
            id = "q_mov_2_1",
            topicId = "top_mov_2",
            question = "¿Cuál es la diferencia entre 'val' y 'var' en Kotlin?",
            optionA = "'val' es de solo lectura (inmutable tras asignarse) y 'var' es mutable y reasignable",
            optionB = "'val' solo almacena texto y 'var' solo números",
            optionC = "'val' es mutable y 'var' inmutable",
            optionD = "No hay diferencia, son sinónimos",
            correctOptionIndex = 0,
            explanation = "'val' define una referencia inmutable (como final en Java), mientras que 'var' permite reasignar nuevos valores."
        ),
        QuizQuestionEntity(
            id = "q_mov_2_2",
            topicId = "top_mov_2",
            question = "¿Qué operador en Kotlin se conoce como 'Elvis operator' para proporcionar un valor por defecto si es nulo?",
            optionA = "!!",
            optionB = "?:",
            optionC = "?.",
            optionD = "as?",
            correctOptionIndex = 1,
            explanation = "El operador '?:' (Elvis) evalúa la expresión izquierda; si es null, retorna el valor a la derecha (ej: name ?: \"Invitado\")."
        ),

        // top_mov_3: Colecciones y Funciones de Orden Superior
        QuizQuestionEntity(
            id = "q_mov_3_1",
            topicId = "top_mov_3",
            question = "¿Qué función de colección en Kotlin transforma cada elemento de una lista aplicando una función y retorna una nueva lista?",
            optionA = "filter",
            optionB = "map",
            optionC = "forEach",
            optionD = "reduce",
            correctOptionIndex = 1,
            explanation = "'map' toma una función de transformación y retorna una lista con los elementos resultantes uno a uno."
        ),
        QuizQuestionEntity(
            id = "q_mov_3_2",
            topicId = "top_mov_3",
            question = "¿Cuál es la diferencia entre 'listOf()' y 'mutableListOf()' en Kotlin?",
            optionA = "'listOf()' produce una lista inmutable (solo lectura), mientras que 'mutableListOf()' permite agregar/quitar elementos",
            optionB = "'listOf()' es sincrónica y 'mutableListOf()' asincrónica",
            optionC = "Ambas son mutables pero tienen diferente rendimiento",
            optionD = "'listOf()' solo acepta strings",
            correctOptionIndex = 0,
            explanation = "En Kotlin las colecciones son inmutables por defecto (List) para evitar efectos secundarios; MutableList permite modificar su contenido."
        ),

        // top_mov_4: POO y Data Classes en Kotlin
        QuizQuestionEntity(
            id = "q_mov_4_1",
            topicId = "top_mov_4",
            question = "¿Qué genera automáticamente el compilador al declarar una 'data class' en Kotlin?",
            optionA = "equals(), hashCode(), toString(), copy() y funciones componentN()",
            optionB = "Una tabla en base de datos SQLite automáticamente",
            optionC = "Una interfaz gráfica en Compose",
            optionD = "Hilos asíncronos en segundo plano",
            correctOptionIndex = 0,
            explanation = "Las data classes generan automáticamente métodos utilitarios de valor como equals, hashCode, toString y copy sin código boilerplate."
        ),

        // top_mov_5: Corrutinas y Flow
        QuizQuestionEntity(
            id = "q_mov_5_1",
            topicId = "top_mov_5",
            question = "¿Qué Dispatcher de Kotlin Coroutines se debe usar para operaciones intensivas de Entrada/Salida como Room o llamadas HTTP?",
            optionA = "Dispatchers.Main",
            optionB = "Dispatchers.IO",
            optionC = "Dispatchers.Default",
            optionD = "Dispatchers.Unconfined",
            correctOptionIndex = 1,
            explanation = "Dispatchers.IO está optimizado con un pool dinámico de hilos para operaciones de disco y red bloqueantes sin congelar la UI."
        ),
        QuizQuestionEntity(
            id = "q_mov_5_2",
            topicId = "top_mov_5",
            question = "¿Qué palabra clave identifica una función que puede pausar y reanudar su ejecución sin bloquear el hilo actual?",
            optionA = "async",
            optionB = "suspend",
            optionC = "defer",
            optionD = "await",
            correctOptionIndex = 1,
            explanation = "Las funciones 'suspend' solo pueden llamarse dentro de una corrutina u otra función suspend, pausando su ejecución sin bloquear el hilo."
        ),

        // top_mov_6: Android Studio y Jetpack Compose
        QuizQuestionEntity(
            id = "q_mov_6_1",
            topicId = "top_mov_6",
            question = "¿Cuál es el paradigma fundamental de Jetpack Compose en comparación con el sistema tradicional de Views/XML?",
            optionA = "Programación Imperativa orientada a objetos",
            optionB = "UI Declarativa donde la interfaz se describe en función del estado actual",
            optionC = "Compilación exclusiva a HTML y JavaScript",
            optionD = "Manipulación manual de árboles DOM",
            correctOptionIndex = 1,
            explanation = "En Compose, describes cómo debe verse la UI en base al estado (declarativo). Cuando el estado cambia, Compose recompone automáticamente."
        ),

        // ══════════════════════════════════════════════════════════════════════════
        // DESARROLLO ORIENTADO A OBJETOS (DSY1102)
        // ══════════════════════════════════════════════════════════════════════════
        // top_poo_1: Paradigma POO y Operadores
        QuizQuestionEntity(
            id = "q_poo_1_1",
            topicId = "top_poo_1",
            question = "¿Cuál de los siguientes NO es uno de los 4 pilares fundamentales de la Programación Orientada a Objetos?",
            optionA = "Encapsulamiento",
            optionB = "Herencia",
            optionC = "Polimorfismo",
            optionD = "Recursividad",
            correctOptionIndex = 3,
            explanation = "Los 4 pilares fundamentales de POO son: Abstracción, Encapsulamiento, Herencia y Polimorfismo."
        ),

        // top_poo_2: Estructuras de Control y Métodos
        QuizQuestionEntity(
            id = "q_poo_2_1",
            topicId = "top_poo_2",
            question = "¿Qué sucede en Java cuando se invoca un método con parámetros de tipo primitivo (ej: int, boolean)?",
            optionA = "Se pasan por referencia directa a la memoria original",
            optionB = "Se pasan por valor (se copia el valor numérico/booleano)",
            optionC = "Se convierten automáticamente a un String",
            optionD = "El método no puede modificarlos ni utilizarlos",
            correctOptionIndex = 1,
            explanation = "En Java, TODOS los tipos primitivos se pasan estrictamente por valor (se genera una copia local dentro del método)."
        ),

        // top_poo_3: Encapsulamiento y Modificadores
        QuizQuestionEntity(
            id = "q_poo_3_1",
            topicId = "top_poo_3",
            question = "¿Qué modificador de acceso restringe la visibilidad de un atributo para que SOLO sea accesible dentro de su propia clase?",
            optionA = "public",
            optionB = "protected",
            optionC = "private",
            optionD = "default (package-private)",
            correctOptionIndex = 2,
            explanation = "'private' oculta los datos de cualquier otra clase, permitiendo controlar el acceso únicamente a través de getters y setters (Encapsulamiento)."
        ),

        // top_poo_4: Herencia y Polimorfismo
        QuizQuestionEntity(
            id = "q_poo_4_1",
            topicId = "top_poo_4",
            question = "¿Qué palabra clave se usa en Java para heredar de una clase padre y qué palabra para implementar una interfaz?",
            optionA = "extends para clases / implements para interfaces",
            optionB = "inherits para clases / uses para interfaces",
            optionC = "implements para clases / extends para interfaces",
            optionD = "super para clases / interface para interfaces",
            correctOptionIndex = 0,
            explanation = "En Java, una clase usa 'extends' para heredar de una única clase padre e 'implements' para una o múltiples interfaces."
        ),
        QuizQuestionEntity(
            id = "q_poo_4_2",
            topicId = "top_poo_4",
            question = "¿Qué anotación se recomienda colocar en Java sobre un método que sobrescribe el comportamiento de una clase base?",
            optionA = "@Overload",
            optionB = "@Override",
            optionC = "@Inherited",
            optionD = "@Replace",
            correctOptionIndex = 1,
            explanation = "@Override informa al compilador que la intención es sobrescribir un método padre, alertando en tiempo de compilación si la firma no coincide."
        ),

        // top_poo_5: Clases Abstractas e Interfaces
        QuizQuestionEntity(
            id = "q_poo_5_1",
            topicId = "top_poo_5",
            question = "¿Se puede instanciar directamente una clase abstracta en Java usando el operador 'new'?",
            optionA = "Sí, siempre que tenga constructor público",
            optionB = "No, una clase abstracta no puede instanciarse directamente; requiere una subclase concreta que implemente sus métodos abstractos",
            optionC = "Solo si no tiene ningún método abstracto",
            optionD = "Solo si implementa Serializable",
            correctOptionIndex = 1,
            explanation = "Las clases abstractas son plantillas incompletas destinadas a servir como base para otras clases y no pueden instanciarse directamente."
        ),

        // top_poo_6: Colecciones y Excepciones
        QuizQuestionEntity(
            id = "q_poo_6_1",
            topicId = "top_poo_6",
            question = "¿Cuál es la principal diferencia entre un 'ArrayList' y un 'HashSet' en Java?",
            optionA = "ArrayList mantiene orden de inserción y permite duplicados; HashSet no garantiza orden y NO permite elementos duplicados",
            optionB = "ArrayList solo acepta enteros y HashSet solo cadenas",
            optionC = "HashSet permite duplicados y ArrayList no",
            optionD = "No hay diferencia funcional",
            correctOptionIndex = 0,
            explanation = "ArrayList es una lista indexada que admite duplicados; Set (HashSet) implementa un conjunto matemático de elementos únicos."
        ),

        // ══════════════════════════════════════════════════════════════════════════
        // TALLER DE BASE DE DATOS (BDY1103 - PL/SQL ORACLE)
        // ══════════════════════════════════════════════════════════════════════════
        // top_bd_1: Estructuras Compuestas (RECORD y VARRAY)
        QuizQuestionEntity(
            id = "q_bd_1_1",
            topicId = "top_bd_1",
            question = "¿Qué directiva se utiliza en PL/SQL para declarar una variable con el mismo tipo de dato que una columna específica de una tabla?",
            optionA = "%ROWTYPE",
            optionB = "%TYPE",
            optionC = "%RECORD",
            optionD = "%VARRAY",
            correctOptionIndex = 1,
            explanation = "'%TYPE' hereda el tipo y tamaño de una columna (ej: emp_id empleados.id%TYPE), mientras que '%ROWTYPE' hereda la estructura de toda la fila."
        ),
        QuizQuestionEntity(
            id = "q_bd_1_2",
            topicId = "top_bd_1",
            question = "¿Qué es un VARRAY en PL/SQL?",
            optionA = "Una tabla física relacional permanente en disco",
            optionB = "Un arreglo homogéneo unidimensional en memoria con un número máximo de elementos definido en su declaración",
            optionC = "Una vista materializada",
            optionD = "Un tipo de cursor que no requiere abrirse",
            correctOptionIndex = 1,
            explanation = "Un VARRAY (Variable-Size Array) almacena una cantidad fija y ordenada de elementos del mismo tipo de datos en memoria."
        ),

        // top_bd_2: Cursores Complejos
        QuizQuestionEntity(
            id = "q_bd_2_1",
            topicId = "top_bd_2",
            question = "¿Cuál es la ventaja de usar un ciclo 'FOR rec IN c_clientes LOOP' frente a OPEN, FETCH y CLOSE manual?",
            optionA = "El ciclo FOR abre, itera y cierra el cursor automáticamente, evitando olvidos de cierre y fugas de memoria",
            optionB = "Ejecuta las consultas en hilos paralelos de la GPU",
            optionC = "Permite ignorar los errores de sintaxis en el SELECT",
            optionD = "Convierte el cursor en un Trigger",
            correctOptionIndex = 0,
            explanation = "El cursor FOR loop simplifica el código al encargarse implícitamente de OPEN, FETCH condicional y CLOSE al finalizar."
        ),

        // top_bd_3: Manejo de Excepciones en PL/SQL
        QuizQuestionEntity(
            id = "q_bd_3_1",
            topicId = "top_bd_3",
            question = "¿Qué instrucción de PL/SQL permite lanzar manualmente una excepción personalizada creada por el desarrollador?",
            optionA = "THROW",
            optionB = "RAISE",
            optionC = "CATCH",
            optionD = "ERROR",
            correctOptionIndex = 1,
            explanation = "En Oracle PL/SQL se utiliza la sentencia 'RAISE nombre_excepcion;' o 'RAISE_APPLICATION_ERROR(-20001, 'mensaje');'."
        ),
        QuizQuestionEntity(
            id = "q_bd_3_2",
            topicId = "top_bd_3",
            question = "¿Qué excepción predefinida se dispara cuando un SELECT INTO en PL/SQL no encuentra ningún registro?",
            optionA = "TOO_MANY_ROWS",
            optionB = "NO_DATA_FOUND",
            optionC = "ZERO_DIVIDE",
            optionD = "INVALID_CURSOR",
            correctOptionIndex = 1,
            explanation = "NO_DATA_FOUND se eleva automáticamente cuando una consulta SELECT INTO no retorna filas."
        ),

        // top_bd_4: Procedimientos y Funciones Almacenadas
        QuizQuestionEntity(
            id = "q_bd_4_1",
            topicId = "top_bd_4",
            question = "¿Cuál es la diferencia fundamental entre una FUNCTION y un PROCEDURE en PL/SQL?",
            optionA = "Una FUNCTION debe retornar obligatoriamente un valor con RETURN y puede usarse en sentencias SQL; un PROCEDURE realiza acciones y retorna mediante parámetros OUT",
            optionB = "Un PROCEDURE no puede recibir parámetros",
            optionC = "Una FUNCTION solo puede ejecutarse en la consola de Linux",
            optionD = "Son exactamente lo mismo con diferente nombre",
            correctOptionIndex = 0,
            explanation = "Las funciones están diseñadas para calcular y retornar un único valor (RETURN type) y pueden ser invocadas dentro de SELECT, WHERE, etc."
        ),

        // top_bd_5: Paquetes y Triggers
        QuizQuestionEntity(
            id = "q_bd_5_1",
            topicId = "top_bd_5",
            question = "¿Cuáles son las dos partes que componen un Package (Paquete) en Oracle PL/SQL?",
            optionA = "Cabecera/Especificación (Package Specification) y Cuerpo (Package Body)",
            optionB = "Trigger y Procedure",
            optionC = "Esquema y Tabla",
            optionD = "Index y Constraint",
            correctOptionIndex = 0,
            explanation = "La especificación define la interfaz pública (firmas de funciones/procedimientos) y el cuerpo contiene la implementación y elementos privados."
        ),
        QuizQuestionEntity(
            id = "q_bd_5_2",
            topicId = "top_bd_5",
            question = "En un Trigger a nivel de fila (FOR EACH ROW) en Oracle, ¿qué calificador hace referencia al nuevo valor que se está insertando o actualizando?",
            optionA = ":OLD",
            optionB = ":NEW",
            optionC = ":CURRENT",
            optionD = ":NEXT",
            correctOptionIndex = 1,
            explanation = "':NEW.columna' contiene el nuevo valor que se va a guardar en la fila, mientras que ':OLD.columna' contiene el valor anterior previo a la modificación."
        ),

        // ══════════════════════════════════════════════════════════════════════════
        // DESARROLLO FULLSTACK II (DSY1104 - HTML5/CSS3/JS/REACT)
        // ══════════════════════════════════════════════════════════════════════════
        // top_fs_1: HTML5 Semántico
        QuizQuestionEntity(
            id = "q_fs_1_1",
            topicId = "top_fs_1",
            question = "¿Por qué se prefieren etiquetas semánticas como <article>, <section>, <nav> y <header> en lugar de genéricos <div>?",
            optionA = "Mejoran la accesibilidad, el SEO y permiten a navegadores y motores de búsqueda comprender la jerarquía del contenido",
            optionB = "Porque aplican estilos CSS automáticamente sin necesidad de clases",
            optionC = "Son obligatorias para que el navegador ejecute JavaScript",
            optionD = "Aumentan la velocidad de descarga de imágenes",
            correctOptionIndex = 0,
            explanation = "El HTML semántico dota de significado a la estructura web, facilitando la lectura a lectores de pantalla y bots de búsqueda."
        ),

        // top_fs_2: CSS3, Box Model, Flexbox y Grid
        QuizQuestionEntity(
            id = "q_fs_2_1",
            topicId = "top_fs_2",
            question = "En CSS Flexbox, ¿qué propiedad alinea los elementos hijos a lo largo del eje principal (main axis)?",
            optionA = "align-items",
            optionB = "justify-content",
            optionC = "align-content",
            optionD = "flex-direction",
            correctOptionIndex = 1,
            explanation = "'justify-content' controla la alineación y distribución del espacio en el eje principal (horizontal por defecto en flex-direction: row)."
        ),

        // top_fs_4: JavaScript Moderno y DOM
        QuizQuestionEntity(
            id = "q_fs_4_1",
            topicId = "top_fs_4",
            question = "¿Qué método de 'fetch()' en JavaScript se utiliza para procesar la respuesta en formato JSON de forma asíncrona?",
            optionA = "response.parse()",
            optionB = "response.json()",
            optionC = "JSON.decode(response)",
            optionD = "response.toObject()",
            correctOptionIndex = 1,
            explanation = "response.json() retorna una Promise que se resuelve con el cuerpo de la respuesta HTTP parseado como objeto JavaScript."
        ),
        QuizQuestionEntity(
            id = "q_fs_4_2",
            topicId = "top_fs_4",
            question = "¿Cuál es la diferencia entre '==' y '===' en JavaScript?",
            optionA = "'==' compara solo valor haciendo coerción implícita de tipo, mientras que '===' compara estricto valor Y tipo de dato",
            optionB = "'===' es para asignar variables y '==' para comparar",
            optionC = "'==' es más rápido y exacto que '==='",
            optionD = "Son idénticos desde ES6",
            correctOptionIndex = 0,
            explanation = "'===' (igualdad estricta) evita errores sutiles de coerción de tipos (ej: 0 == '' es true con ==, pero false con ===)."
        ),

        // ══════════════════════════════════════════════════════════════════════════
        // ESTADÍSTICA DESCRIPTIVA (MAT4141 - PYTHON/PANDAS)
        // ══════════════════════════════════════════════════════════════════════════
        // top_est_1: Python y Pandas
        QuizQuestionEntity(
            id = "q_est_1_1",
            topicId = "top_est_1",
            question = "¿Qué método en Pandas muestra un resumen estadístico completo (conteo, media, desv. estándar, cuartiles, min/max) de las columnas numéricas?",
            optionA = "df.info()",
            optionB = "df.describe()",
            optionC = "df.summary()",
            optionD = "df.head()",
            correctOptionIndex = 1,
            explanation = "df.describe() calcula instantáneamente los principales estadísticos descriptivos de todas las columnas numéricas de un DataFrame."
        ),
        QuizQuestionEntity(
            id = "q_est_1_2",
            topicId = "top_est_1",
            question = "¿Cuál es la diferencia entre una Serie (Series) y un DataFrame en Pandas?",
            optionA = "Una Serie es una estructura unidimensional (1 sola columna con índice) y un DataFrame es bidimensional tabular (filas y columnas)",
            optionB = "La Serie almacena gráficos y el DataFrame números",
            optionC = "El DataFrame no tiene índices",
            optionD = "Una Serie solo admite números enteros",
            correctOptionIndex = 0,
            explanation = "Una Series es como un array unidimensional etiquetado; un DataFrame es una tabla 2D compuesta por múltiples Series como columnas."
        ),

        // top_est_2: Clasificación de Variables y Frecuencias
        QuizQuestionEntity(
            id = "q_est_2_1",
            topicId = "top_est_2",
            question = "La variable 'Nivel de satisfacción de un cliente (Bajo, Medio, Alto)' se clasifica como:",
            optionA = "Cuantitativa discreta",
            optionB = "Cualitativa ordinal",
            optionC = "Cualitativa nominal",
            optionD = "Cuantitativa continua",
            correctOptionIndex = 1,
            explanation = "Es cualitativa (no numérica en origen) y ordinal porque existe un orden o jerarquía natural evidente entre sus categorías."
        ),

        // top_est_3: Medidas de Tendencia Central
        QuizQuestionEntity(
            id = "q_est_3_1",
            topicId = "top_est_3",
            question = "¿Qué medida de tendencia central es la más adecuada cuando el conjunto de datos contiene valores atípicos extremos (outliers)?",
            optionA = "La Media Aritmética (promedio)",
            optionB = "La Mediana (valor central)",
            optionC = "El Coeficiente de Variación",
            optionD = "La Varianza",
            correctOptionIndex = 1,
            explanation = "La Mediana es robusta ante valores extremos, ya que depende de la posición central de los datos ordenados y no de la magnitud de los extremos."
        ),

        // top_est_4: Medidas de Dispersión
        QuizQuestionEntity(
            id = "q_est_4_1",
            topicId = "top_est_4",
            question = "¿Por qué la Desviación Estándar es más utilizada para interpretar resultados que la Varianza?",
            optionA = "Porque se expresa en las MISMAS unidades de medida originales de la variable (al ser la raíz cuadrada de la varianza)",
            optionB = "Porque siempre da un número negativo",
            optionC = "Porque no requiere ordenar los datos",
            optionD = "Porque elimina todos los valores duplicados",
            correctOptionIndex = 0,
            explanation = "La varianza tiene unidades al cuadrado (ej: metros² o pesos²); la desviación estándar recupera la unidad original (metros o pesos)."
        ),

        // ══════════════════════════════════════════════════════════════════════════
        // ÉTICA PARA EL TRABAJO (EAY4730)
        // ══════════════════════════════════════════════════════════════════════════
        // top_et_1: Dimensión Moral en Informática
        QuizQuestionEntity(
            id = "q_et_1_1",
            topicId = "top_et_1",
            question = "¿Qué establece el principio de 'Responsabilidad Profesional' en el desarrollo de software y sistemas?",
            optionA = "Garantizar que el sistema funcione únicamente durante la entrega del proyecto",
            optionB = "Velar por la seguridad, confidencialidad, fiabilidad del código y el bienestar de los usuarios afectados por el software",
            optionC = "Desentenderse de los fallos una vez firmado el contrato de entrega",
            optionD = "Cobrar tarifas superiores a la media del mercado",
            correctOptionIndex = 1,
            explanation = "El deber ético del informático implica velar por el impacto social, seguridad y confidencialidad de las soluciones creadas."
        ),

        // top_et_2: Dilemas Éticos
        QuizQuestionEntity(
            id = "q_et_2_1",
            topicId = "top_et_2",
            question = "Si un superior te pide implementar un mecanismo no documentado para recolectar datos de usuarios sin su consentimiento, ¿cuál es la conducta ética correcta?",
            optionA = "Implementarlo en silencio para evitar conflictos laborales",
            optionB = "Advertir formalmente sobre la violación de privacidad, normativas legales (como GDPR o Ley de Datos) y negarse a vulnerar derechos",
            optionC = "Vender los datos a un tercero para beneficio personal",
            optionD = "Renunciar sin explicar ningún motivo",
            correctOptionIndex = 1,
            explanation = "Los códigos deontológicos de la informática (ACM / IEEE) exigen rechazar la recopilación ilegítima y no consentida de datos personales."
        ),

        // top_et_3: Privacidad y Códigos Deontológicos
        QuizQuestionEntity(
            id = "q_et_3_1",
            topicId = "top_et_3",
            question = "¿Qué organismo internacional publica el 'Código de Ética y Conducta Profesional' más reconocido en la industria informática?",
            optionA = "ACM (Association for Computing Machinery) e IEEE Computer Society",
            optionB = "La FIFA",
            optionC = "La Organización Mundial del Comercio",
            optionD = "El Consorcio W3C exclusivamente para HTML",
            correctOptionIndex = 0,
            explanation = "El código conjunto ACM/IEEE establece los 8 principios fundamentales del ejercicio ético en la ingeniería de software."
        )
    )
}
