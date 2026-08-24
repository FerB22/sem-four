package com.example.semfour.data.local

import com.example.semfour.data.local.entity.QuizQuestionEntity

/**
 * Catálogo oficial de preguntas de selección múltiple (Active Recall / Quiz)
 * para los temas de las 6 asignaturas del 4.º Semestre (Duoc UC),
 * enriquecido con el material, guías, PPTs y cuestionarios extraídos de AVA.
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
        QuizQuestionEntity(
            id = "q_mov_1_3",
            topicId = "top_mov_1",
            question = "¿En qué se diferencia el desarrollo móvil Nativo del Multiplataforma / Híbrido?",
            optionA = "El nativo se programa con las herramientas y lenguajes oficiales del fabricante (Kotlin/Swift) accediendo al 100% de las APIs del hardware con máximo rendimiento",
            optionB = "El nativo no puede acceder a la cámara ni al GPS",
            optionC = "El desarrollo híbrido siempre es más rápido y fluido que el nativo",
            optionD = "El nativo requiere ejecutar un navegador web dentro de la app",
            correctOptionIndex = 0,
            explanation = "El desarrollo nativo utiliza las herramientas oficiales de Google y Apple, garantizando el mejor rendimiento y acceso directo al hardware del dispositivo."
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
        QuizQuestionEntity(
            id = "q_mov_2_3",
            topicId = "top_mov_2",
            question = "En Kotlin, ¿cómo se comporta la estructura 'when' y 'if' respecto a Java?",
            optionA = "En Kotlin pueden ser usadas como expresiones que retornan un valor directamente",
            optionB = "Solo pueden ejecutarse como sentencias sin retornar nada",
            optionC = "'when' requiere obligatoriamente la sentencia 'break' en cada caso",
            optionD = "No admiten ramas 'else'",
            correctOptionIndex = 0,
            explanation = "En Kotlin, tanto 'if' como 'when' son expresiones que pueden devolver un valor y asignarse directamente a una variable (ej: val result = when (x) { ... })."
        ),

        // top_mov_3: Colecciones y Funciones en Kotlin
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
        QuizQuestionEntity(
            id = "q_mov_3_3",
            topicId = "top_mov_3",
            question = "¿Qué hace la función 'groupBy' en una colección de Kotlin?",
            optionA = "Agrupa los elementos en un Map según una clave calculada por la función lambda",
            optionB = "Elimina todos los elementos impares",
            optionC = "Ordena la lista en orden descendente",
            optionD = "Suma todos los números acumulativamente",
            correctOptionIndex = 0,
            explanation = "'groupBy' devuelve un Map<K, List<T>> agrupando los elementos de la colección según el valor devuelto por la expresión lambda."
        ),

        // top_mov_4: POO y Control de Errores en Kotlin
        QuizQuestionEntity(
            id = "q_mov_4_1",
            topicId = "top_mov_4",
            question = "¿Qué palabra clave es obligatoria en Kotlin sobre una clase padre para permitir que otras clases hereden de ella?",
            optionA = "open",
            optionB = "public",
            optionC = "abstract",
            optionD = "extendable",
            correctOptionIndex = 0,
            explanation = "Por defecto, todas las clases y métodos en Kotlin son 'final' (cerradas a la herencia). Para permitir herencia, deben declararse explícitamente como 'open'."
        ),
        QuizQuestionEntity(
            id = "q_mov_4_2",
            topicId = "top_mov_4",
            question = "¿Qué ventaja ofrece el uso de la clase 'Result' (Result.success / Result.failure) frente al try-catch tradicional?",
            optionA = "Permite modelar fallos esperados de forma funcional y explícita en la firma del método sin interrumpir el flujo con excepciones no controladas",
            optionB = "Acelera el cálculo de operaciones matemáticas",
            optionC = "Evita tener que declarar variables inmutables",
            optionD = "Convierte automáticamente código Kotlin a SQL",
            correctOptionIndex = 0,
            explanation = "La clase 'Result' envuelve el éxito o fallo de una operación, permitiendo un tratamiento elegante y seguro de errores previsibles como peticiones de red o validaciones."
        ),
        QuizQuestionEntity(
            id = "q_mov_4_3",
            topicId = "top_mov_4",
            question = "¿Qué modificador de visibilidad en Kotlin restringe el acceso para que SOLO sea visible dentro del mismo módulo de compilación?",
            optionA = "internal",
            optionB = "private",
            optionC = "protected",
            optionD = "package-private",
            correctOptionIndex = 0,
            explanation = "'internal' permite que una clase, función o propiedad sea accesible desde cualquier archivo dentro del mismo módulo de compilación (ej. módulo de Gradle)."
        ),

        // top_mov_5: Corrutinas y Sintaxis Avanzada
        QuizQuestionEntity(
            id = "q_mov_5_1",
            topicId = "top_mov_5",
            question = "¿Cuál es la diferencia entre los constructores de corrutinas 'launch' y 'async' en Kotlin?",
            optionA = "'launch' inicia una corrutina tipo 'fire-and-forget' sin retornar resultado directo; 'async' retorna un Deferred<T> cuyo resultado se obtiene con .await()",
            optionB = "'launch' solo funciona en el hilo principal y 'async' en segundo plano",
            optionC = "'async' no puede pausarse con suspend",
            optionD = "Son idénticos pero 'launch' es para Java",
            correctOptionIndex = 0,
            explanation = "'launch' devuelve un Job para control de ejecución, mientras que 'async' devuelve un Deferred<T> que permite esperar y obtener el valor resultante mediante '.await()'."
        ),
        QuizQuestionEntity(
            id = "q_mov_5_2",
            topicId = "top_mov_5",
            question = "¿Para qué se utilizan principalmente las 'Sealed Classes' (clases selladas) en Kotlin?",
            optionA = "Para representar jerarquías cerradas y modelar estados finitos de UI (Loading, Success, Error) evaluables exhaustivamente en un 'when'",
            optionB = "Para encriptar la base de datos local SQLite",
            optionC = "Para crear conexiones Bluetooth entre dispositivos",
            optionD = "Para forzar la recarga de imágenes en segundo plano",
            correctOptionIndex = 0,
            explanation = "Las Sealed Classes restringen la herencia a un conjunto conocido de subclases, permitiendo que el compilador verifique que todos los estados posibles están cubiertos en un 'when' sin requerir 'else'."
        ),
        QuizQuestionEntity(
            id = "q_mov_5_3",
            topicId = "top_mov_5",
            question = "¿Qué diferencia hay entre las scope functions 'apply' y 'let' en Kotlin?",
            optionA = "'apply' ejecuta la configuración sobre el receptor y retorna el mismo objeto; 'let' recibe el objeto como parámetro (it) y retorna el resultado de la expresión lambda",
            optionB = "'apply' solo se usa con nulos y 'let' con listas",
            optionC = "'let' modifica variables globales y 'apply' variables locales",
            optionD = "No hay diferencia, ambas retornan Unit",
            correctOptionIndex = 0,
            explanation = "'apply' es ideal para configurar propiedades de un objeto al crearlo (retorna 'this'), mientras que 'let' se usa frecuentemente con safe-calls (?.) para transformar o procesar un valor no nulo."
        ),

        // top_mov_6: Android Studio y Jetpack Compose
        QuizQuestionEntity(
            id = "q_mov_6_1",
            topicId = "top_mov_6",
            question = "¿Cuál es el paradigma fundamental de Jetpack Compose en comparación con el sistema tradicional de Views/XML?",
            optionA = "UI Declarativa donde la interfaz se describe en función del estado actual y se recompone automáticamente al haber cambios",
            optionB = "Programación Imperativa orientada a objetos con findViewById",
            optionC = "Compilación exclusiva a HTML y JavaScript",
            optionD = "Manipulación manual de árboles DOM",
            correctOptionIndex = 0,
            explanation = "En Compose, describes cómo debe verse la UI en base al estado (declarativo). Cuando el estado cambia, Compose recompone automáticamente los componentes afectados."
        ),

        // ══════════════════════════════════════════════════════════════════════════
        // DESARROLLO ORIENTADO A OBJETOS (DSY1102)
        // ══════════════════════════════════════════════════════════════════════════
        // top_poo_1: Paradigma POO y Abstracción
        QuizQuestionEntity(
            id = "q_poo_1_1",
            topicId = "top_poo_1",
            question = "¿En qué consiste el principio de 'Abstracción' al modelar una clase a partir de un caso del mundo real (ej. Libro en una biblioteca)?",
            optionA = "Identificar y capturar únicamente los atributos y acciones esenciales para el problema a resolver, omitiendo detalles irrelevantes",
            optionB = "Copiar todos los detalles físicos de los objetos reales sin excluir nada",
            optionC = "Crear únicamente métodos estáticos sin atributos",
            optionD = "Convertir el código Java a lenguaje de máquina directamente",
            correctOptionIndex = 0,
            explanation = "La abstracción permite concentrarse en lo importante (ej. título, autor, isbn, disponibilidad) e ignorar detalles innecesarios (peso del papel, tipografía)."
        ),
        QuizQuestionEntity(
            id = "q_poo_1_2",
            topicId = "top_poo_1",
            question = "¿Cuál es la relación fundamental entre una 'Clase' y un 'Objeto' en Java?",
            optionA = "La Clase es la plantilla/molde que define la estructura y el Objeto es una instancia concreta creada en memoria",
            optionB = "El Objeto es la plantilla y la Clase es la instancia",
            optionC = "Son exactamente lo mismo sin ninguna distinción técnica",
            optionD = "Una clase solo puede tener una única instancia en toda la aplicación",
            correctOptionIndex = 0,
            explanation = "La clase define los atributos y métodos teóricos; el objeto es la instancia viva creada en tiempo de ejecución mediante 'new'."
        ),
        QuizQuestionEntity(
            id = "q_poo_1_3",
            topicId = "top_poo_1",
            question = "¿Cuál de los siguientes NO es uno de los 4 pilares fundamentales de la Programación Orientada a Objetos?",
            optionA = "Encapsulamiento",
            optionB = "Herencia",
            optionC = "Polimorfismo",
            optionD = "Recursividad",
            correctOptionIndex = 3,
            explanation = "Los 4 pilares fundamentales de POO son: Abstracción, Encapsulamiento, Herencia y Polimorfismo."
        ),

        // top_poo_2: Tipos, Estructuras de Control y Métodos
        QuizQuestionEntity(
            id = "q_poo_2_1",
            topicId = "top_poo_2",
            question = "¿Qué sucede en Java cuando se invoca un método con parámetros de tipo primitivo (ej: int, boolean)?",
            optionA = "Se pasan estrictamente por valor (se genera una copia local del dato dentro del método)",
            optionB = "Se pasan por referencia directa a la memoria original",
            optionC = "Se convierten automáticamente a un String",
            optionD = "El método no puede modificarlos ni utilizarlos",
            correctOptionIndex = 0,
            explanation = "En Java, TODOS los tipos primitivos se pasan estrictamente por valor (se genera una copia del valor dentro del marco del método)."
        ),
        QuizQuestionEntity(
            id = "q_poo_2_2",
            topicId = "top_poo_2",
            question = "¿Cuál es la diferencia principal entre un ciclo 'while' y un ciclo 'do-while' en Java?",
            optionA = "El ciclo 'do-while' ejecuta su bloque al menos una vez antes de evaluar la condición, mientras que 'while' evalúa la condición al inicio",
            optionB = "'while' solo admite números enteros y 'do-while' cadenas",
            optionC = "'do-while' no permite utilizar la instrucción 'break'",
            optionD = "Son idénticos y el compilador los transforma en la misma instrucción",
            correctOptionIndex = 0,
            explanation = "En 'do-while', la condición se encuentra al final del bloque, garantizando una primera iteración obligatoria independientemente de la condición."
        ),
        QuizQuestionEntity(
            id = "q_poo_2_3",
            topicId = "top_poo_2",
            question = "¿Qué es la 'Sobrecarga de Métodos' (Method Overloading) en Java?",
            optionA = "Definir múltiples métodos en una misma clase con el mismo nombre pero diferente lista o tipo de parámetros",
            optionB = "Reescribir un método de la clase padre en una subclase",
            optionC = "Llenar la memoria RAM con demasiadas llamadas a funciones",
            optionD = "Un error de compilación por duplicidad de nombres",
            correctOptionIndex = 0,
            explanation = "La sobrecarga permite usar el mismo nombre para métodos que realizan operaciones similares con diferentes tipos o cantidades de argumentos."
        ),

        // top_poo_3: Clases, Constructores y Encapsulamiento
        QuizQuestionEntity(
            id = "q_poo_3_1",
            topicId = "top_poo_3",
            question = "¿Para qué se utiliza la palabra clave 'this' en un constructor o método de Java?",
            optionA = "Para referenciar explícitamente los atributos o métodos de la instancia actual y distinguirlos de los parámetros con igual nombre",
            optionB = "Para importar librerías externas",
            optionC = "Para pausar el hilo de ejecución",
            optionD = "Para destruir el objeto en memoria",
            correctOptionIndex = 0,
            explanation = "'this' hace referencia a la instancia actual del objeto (ej: this.nombre = nombre asigna el parámetro al atributo de la clase)."
        ),
        QuizQuestionEntity(
            id = "q_poo_3_2",
            topicId = "top_poo_3",
            question = "¿Qué modificador de acceso restringe la visibilidad de un atributo para que SOLO sea accesible dentro de su propia clase?",
            optionA = "private",
            optionB = "public",
            optionC = "protected",
            optionD = "default (package-private)",
            correctOptionIndex = 0,
            explanation = "'private' oculta los datos de cualquier otra clase, permitiendo controlar el acceso únicamente a través de getters y setters (Encapsulamiento)."
        ),

        // top_poo_4: Herencia y Polimorfismo
        QuizQuestionEntity(
            id = "q_poo_4_1",
            topicId = "top_poo_4",
            question = "¿Cómo invoca una subclase en Java al constructor de su clase padre?",
            optionA = "Mediante la llamada 'super(parametros);' como primera línea del constructor",
            optionB = "Llamando a 'this(parametros);'",
            optionC = "Con el comando 'Parent.create()'",
            optionD = "No se puede invocar el constructor padre en Java",
            correctOptionIndex = 0,
            explanation = "'super()' invoca el constructor de la superclase y debe ser obligatoriamente la primera sentencia dentro del constructor de la subclase."
        ),
        QuizQuestionEntity(
            id = "q_poo_4_2",
            topicId = "top_poo_4",
            question = "¿Qué anotación se recomienda colocar en Java sobre un método que sobrescribe el comportamiento de una clase base?",
            optionA = "@Override",
            optionB = "@Overload",
            optionC = "@Inherited",
            optionD = "@Replace",
            correctOptionIndex = 0,
            explanation = "@Override informa al compilador que la intención es sobrescribir un método padre, alertando en tiempo de compilación si la firma no coincide."
        ),

        // top_poo_5: Clases Abstractas e Interfaces
        QuizQuestionEntity(
            id = "q_poo_5_1",
            topicId = "top_poo_5",
            question = "¿Cuál es una diferencia clave entre una 'clase abstracta' y una 'interfaz' en Java?",
            optionA = "Una clase solo puede heredar de una clase abstracta ('extends'), pero puede implementar múltiples interfaces ('implements')",
            optionB = "Las interfaces pueden tener constructores públicos y las abstractas no",
            optionC = "Una clase abstracta no puede contener ningún método implementado",
            optionD = "Las interfaces solo pueden ser privadas",
            correctOptionIndex = 0,
            explanation = "Java no soporta herencia múltiple de clases, pero sí permite que una clase implemente múltiples interfaces para cumplir varios contratos."
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
        QuizQuestionEntity(
            id = "q_poo_6_2",
            topicId = "top_poo_6",
            question = "En la estructura 'try-catch-finally' de Java, ¿cuándo se ejecuta el bloque 'finally'?",
            optionA = "Siempre se ejecuta, haya ocurrido o no una excepción en el bloque try",
            optionB = "Solo si ocurrió un error en el catch",
            optionC = "Únicamente si la excepción es de tipo NullPointerException",
            optionD = "Solo si no se lanza ninguna excepción",
            correctOptionIndex = 0,
            explanation = "El bloque 'finally' se ejecuta incondicionalmente, lo que lo hace ideal para cerrar conexiones, archivos o liberar recursos."
        ),

        // ══════════════════════════════════════════════════════════════════════════
        // TALLER DE BASE DE DATOS (BDY1103 - PL/SQL ORACLE)
        // ══════════════════════════════════════════════════════════════════════════
        // top_bd_1: Estructuras Compuestas (RECORD y VARRAY)
        QuizQuestionEntity(
            id = "q_bd_1_1",
            topicId = "top_bd_1",
            question = "¿Qué directiva se utiliza en PL/SQL para declarar una variable con la estructura completa de una fila de tabla?",
            optionA = "%ROWTYPE",
            optionB = "%TYPE",
            optionC = "%RECORD",
            optionD = "%VARRAY",
            correctOptionIndex = 0,
            explanation = "'%ROWTYPE' hereda la estructura y tipos de toda una fila de la tabla, mientras que '%TYPE' hereda solo una columna individual."
        ),
        QuizQuestionEntity(
            id = "q_bd_1_2",
            topicId = "top_bd_1",
            question = "¿Cuáles son las características principales del tipo de dato 'VARRAY' en PL/SQL?",
            optionA = "Almacena una colección en memoria con elementos del mismo tipo y un número máximo de elementos fijo definido en su declaración",
            optionB = "Es una tabla temporal que no admite números",
            optionC = "Puede crecer indefinidamente sin ningún límite establecido",
            optionD = "Solo se puede usar dentro de funciones Java",
            correctOptionIndex = 0,
            explanation = "Un VARRAY (Variable-Size Array) almacena una cantidad acotada y ordenada de elementos homogéneos en memoria."
        ),

        // top_bd_2: Cursores Complejos y con Parámetros
        QuizQuestionEntity(
            id = "q_bd_2_1",
            topicId = "top_bd_2",
            question = "¿Cuál es la principal ventaja de utilizar cursores explícitos con parámetros en PL/SQL?",
            optionA = "Permiten reutilizar la misma consulta pasándole diferentes valores para seleccionar datos dinámicamente según criterios variables",
            optionB = "Hacen que la base de datos ignore las claves foráneas",
            optionC = "Evitan tener que declarar tipos de datos en la base",
            optionD = "Convierten la consulta en una tabla física",
            correctOptionIndex = 0,
            explanation = "Los cursores con parámetros aumentan la flexibilidad y modularidad al filtrar registros dinámicamente con diferentes argumentos."
        ),
        QuizQuestionEntity(
            id = "q_bd_2_2",
            topicId = "top_bd_2",
            question = "¿Por qué es fundamental cerrar un cursor explícito abierto manualmente mediante 'CLOSE'?",
            optionA = "Para liberar la memoria del área de contexto y los recursos asignados en el servidor Oracle",
            optionB = "Para que la tabla no se borre del disco",
            optionC = "Para que los datos se guarden permanentemente en la nube",
            optionD = "No es necesario cerrar cursores explícitos",
            correctOptionIndex = 0,
            explanation = "Dejar cursores abiertos agota los recursos del servidor y puede provocar el error ORA-01000 (maximum open cursors exceeded)."
        ),

        // top_bd_3: Manejo de Excepciones Predefinidas y Definidas por el Usuario
        QuizQuestionEntity(
            id = "q_bd_3_1",
            topicId = "top_bd_3",
            question = "¿Qué excepción predefinida se dispara cuando una consulta 'SELECT INTO' en PL/SQL no encuentra ningún registro coincidente?",
            optionA = "NO_DATA_FOUND",
            optionB = "TOO_MANY_ROWS",
            optionC = "ZERO_DIVIDE",
            optionD = "INVALID_CURSOR",
            correctOptionIndex = 0,
            explanation = "NO_DATA_FOUND se eleva automáticamente cuando una consulta SELECT INTO no retorna filas."
        ),
        QuizQuestionEntity(
            id = "q_bd_3_2",
            topicId = "top_bd_3",
            question = "¿Qué instrucción de PL/SQL permite lanzar manualmente una excepción personalizada creada por el desarrollador?",
            optionA = "RAISE mi_excepcion; o RAISE_APPLICATION_ERROR(-20001, 'Mensaje');",
            optionB = "THROW mi_excepcion;",
            optionC = "CATCH mi_excepcion;",
            optionD = "TRY mi_excepcion;",
            correctOptionIndex = 0,
            explanation = "En Oracle PL/SQL se utiliza la sentencia 'RAISE nombre_excepcion;' o el procedimiento 'RAISE_APPLICATION_ERROR' con códigos entre -20000 y -20999."
        ),
        QuizQuestionEntity(
            id = "q_bd_3_3",
            topicId = "top_bd_3",
            question = "¿Qué excepción predefinida ocurre cuando un 'SELECT INTO' devuelve más de una fila?",
            optionA = "TOO_MANY_ROWS",
            optionB = "NO_DATA_FOUND",
            optionC = "CURSOR_ALREADY_OPEN",
            optionD = "STORAGE_ERROR",
            correctOptionIndex = 0,
            explanation = "SELECT INTO espera exactamente 1 registro escalar. Si la consulta retorna 2 o más registros, se dispara TOO_MANY_ROWS."
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
            optionA = ":NEW",
            optionB = ":OLD",
            optionC = ":CURRENT",
            optionD = ":NEXT",
            correctOptionIndex = 0,
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
        QuizQuestionEntity(
            id = "q_fs_1_2",
            topicId = "top_fs_1",
            question = "¿Cómo se vincula una hoja de estilos CSS externa a un documento HTML?",
            optionA = "Mediante la etiqueta <link rel=\"stylesheet\" href=\"estilos.css\"> dentro de la sección <head>",
            optionB = "Con la etiqueta <style src=\"estilos.css\"> en el <body>",
            optionC = "Con la etiqueta <script href=\"estilos.css\"> en el <footer>",
            optionD = "Usando un iframe",
            correctOptionIndex = 0,
            explanation = "La etiqueta <link> con atributo rel=\"stylesheet\" se ubica en el <head> para cargar y renderizar los estilos antes de pintar la UI."
        ),

        // top_fs_2: CSS3, Box Model, Flexbox y Grid
        QuizQuestionEntity(
            id = "q_fs_2_1",
            topicId = "top_fs_2",
            question = "En CSS Flexbox, ¿qué propiedad alinea los elementos hijos a lo largo del eje principal (main axis)?",
            optionA = "justify-content",
            optionB = "align-items",
            optionC = "align-content",
            optionD = "flex-direction",
            correctOptionIndex = 0,
            explanation = "'justify-content' controla la alineación y distribución del espacio en el eje principal (horizontal por defecto en flex-direction: row)."
        ),
        QuizQuestionEntity(
            id = "q_fs_2_2",
            topicId = "top_fs_2",
            question = "¿Qué es una función 'linear-gradient()' en CSS y cuál es su propósito visual?",
            optionA = "Crea una transición progresiva y suave entre dos o más colores para fondos sin requerir imágenes externas pesadas",
            optionB = "Calcula la velocidad de descarga de un video",
            optionC = "Aplica bordes redondeados a una tabla",
            optionD = "Centra el texto automáticamente en la pantalla",
            correctOptionIndex = 0,
            explanation = "linear-gradient crea degradados de color en background-image, ofreciendo diseño moderno y liviano sin peticiones de red adicionales."
        ),
        QuizQuestionEntity(
            id = "q_fs_2_3",
            topicId = "top_fs_2",
            question = "¿Cómo se asegura que una página web sea adaptable (responsive) a dispositivos móviles?",
            optionA = "Configurando el meta viewport en el <head> y utilizando Media Queries (@media) con unidades relativas (%, rem, flex, grid)",
            optionB = "Creando una página HTML distinta para cada modelo de celular",
            optionC = "Usando solo medidas fijas en píxeles (px)",
            optionD = "Desactivando el scroll vertical",
            correctOptionIndex = 0,
            explanation = "El viewport meta tag y las Media Queries permiten adaptar el layout, tipografía y visibilidad (como el menú hamburguesa) a diferentes anchos de pantalla."
        ),

        // top_fs_4: JavaScript Moderno y DOM
        QuizQuestionEntity(
            id = "q_fs_4_1",
            topicId = "top_fs_4",
            question = "¿Qué método de 'fetch()' en JavaScript se utiliza para procesar la respuesta en formato JSON de forma asíncrona?",
            optionA = "response.json()",
            optionB = "response.parse()",
            optionC = "JSON.decode(response)",
            optionD = "response.toObject()",
            correctOptionIndex = 0,
            explanation = "response.json() retorna una Promise que se resuelve con el cuerpo de la respuesta HTTP parseado como objeto JavaScript."
        ),
        QuizQuestionEntity(
            id = "q_fs_4_2",
            topicId = "top_fs_4",
            question = "¿Cuál es la diferencia entre '==' y '===' en JavaScript?",
            optionA = "'===' compara estrictamente valor Y tipo de dato, mientras que '==' compara solo valor haciendo coerción implícita de tipos",
            optionB = "'===' es para asignar variables y '==' para comparar",
            optionC = "'==' es más exacto que '==='",
            optionD = "Son idénticos desde ES6",
            correctOptionIndex = 0,
            explanation = "'===' (igualdad estricta) evita errores sutiles de coerción de tipos (ej: 0 == '' es true con ==, pero false con ===)."
        ),
        QuizQuestionEntity(
            id = "q_fs_4_3",
            topicId = "top_fs_4",
            question = "¿Cuál es el rol de JavaScript en la interacción del menú hamburguesa en un sitio web responsivo?",
            optionA = "Escuchar el evento 'click' para alternar clases CSS (como '.active') que muestran u ocultan el menú en dispositivos móviles",
            optionB = "Descargar el código HTML de nuevo desde el servidor",
            optionC = "Comprimir los archivos CSS en tiempo real",
            optionD = "Reiniciar la sesión del usuario",
            correctOptionIndex = 0,
            explanation = "JavaScript manipula dinámicamente las clases del DOM mediante event listeners para abrir/cerrar menús desplegables e interactivos."
        ),

        // ══════════════════════════════════════════════════════════════════════════
        // ESTADÍSTICA DESCRIPTIVA (MAT4141 - PYTHON/PANDAS)
        // ══════════════════════════════════════════════════════════════════════════
        // top_est_1: Python y Pandas
        QuizQuestionEntity(
            id = "q_est_1_1",
            topicId = "top_est_1",
            question = "¿Qué método en Pandas muestra un resumen estadístico completo (conteo, media, desv. estándar, cuartiles, min/max) de las columnas numéricas?",
            optionA = "df.describe()",
            optionB = "df.info()",
            optionC = "df.summary()",
            optionD = "df.head()",
            correctOptionIndex = 0,
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
            optionA = "Cualitativa ordinal",
            optionB = "Cuantitativa discreta",
            optionC = "Cualitativa nominal",
            optionD = "Cuantitativa continua",
            correctOptionIndex = 0,
            explanation = "Es cualitativa (no numérica en origen) y ordinal porque existe un orden o jerarquía natural evidente entre sus categorías."
        ),

        // top_est_3: Medidas de Tendencia Central
        QuizQuestionEntity(
            id = "q_est_3_1",
            topicId = "top_est_3",
            question = "¿Qué medida de tendencia central es la más adecuada cuando el conjunto de datos contiene valores atípicos extremos (outliers)?",
            optionA = "La Mediana (valor central)",
            optionB = "La Media Aritmética (promedio)",
            optionC = "El Coeficiente de Variación",
            optionD = "La Varianza",
            correctOptionIndex = 0,
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
            optionA = "Velar por la seguridad, confidencialidad, fiabilidad del código y el bienestar de los usuarios afectados por el software",
            optionB = "Garantizar que el sistema funcione únicamente durante la entrega del proyecto",
            optionC = "Desentenderse de los fallos una vez firmado el contrato de entrega",
            optionD = "Cobrar tarifas superiores a la media del mercado",
            correctOptionIndex = 0,
            explanation = "El deber ético del informático implica velar por el impacto social, seguridad y confidencialidad de las soluciones creadas."
        ),

        // top_et_2: Dilemas Éticos
        QuizQuestionEntity(
            id = "q_et_2_1",
            topicId = "top_et_2",
            question = "Si un superior te pide implementar un mecanismo no documentado para recolectar datos de usuarios sin su consentimiento, ¿cuál es la conducta ética correcta?",
            optionA = "Advertir formalmente sobre la violación de privacidad, normativas legales (como GDPR o Ley de Datos) y negarse a vulnerar derechos",
            optionB = "Implementarlo en silencio para evitar conflictos laborales",
            optionC = "Vender los datos a un tercero para beneficio personal",
            optionD = "Renunciar sin explicar ningún motivo",
            correctOptionIndex = 0,
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
