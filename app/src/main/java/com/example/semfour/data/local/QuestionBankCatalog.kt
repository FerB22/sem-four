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
        QuizQuestionEntity(
            id = "q_bd_1_3",
            topicId = "top_bd_1",
            question = "¿Cómo se accede o asigna un valor a un campo individual dentro de una variable de tipo RECORD en PL/SQL?",
            optionA = "Mediante la notación de punto: variable_record.nombre_campo",
            optionB = "Con corchetes: variable_record['campo']",
            optionC = "Con una flecha: variable_record->campo",
            optionD = "No se puede acceder individualmente",
            correctOptionIndex = 0,
            explanation = "Al igual que en los objetos de programación, en PL/SQL se utiliza el punto (ej: v_empleado.salario := 500000;)."
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
        QuizQuestionEntity(
            id = "q_bd_2_3",
            topicId = "top_bd_2",
            question = "¿Qué estructura de control simplifica el manejo de un cursor abriéndolo, extrayendo las filas (fetch) y cerrándolo automáticamente?",
            optionA = "El ciclo FOR registro IN nombre_cursor LOOP ... END LOOP;",
            optionB = "El ciclo WHILE (cursor == open)",
            optionC = "La sentencia IF MATCH",
            optionD = "El comando TRY-CATCH",
            correctOptionIndex = 0,
            explanation = "El cursor FOR LOOP gestiona de forma automática la apertura (OPEN), extracción de registros (FETCH) y cierre (CLOSE) del cursor sin código manual."
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
        QuizQuestionEntity(
            id = "q_bd_4_2",
            topicId = "top_bd_4",
            question = "¿Qué modo de parámetro permite enviar un valor al procedimiento, modificarlo dentro y devolver el nuevo valor hacia quien lo llamó?",
            optionA = "IN OUT",
            optionB = "IN (modo por defecto, solo lectura)",
            optionC = "OUT (solo escritura de salida)",
            optionD = "RETURN ONLY",
            correctOptionIndex = 0,
            explanation = "El modo 'IN OUT' pasa una variable tanto de entrada como de salida, permitiendo que el procedimiento lea su valor inicial y lo sobreescriba con un resultado."
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
        QuizQuestionEntity(
            id = "q_bd_5_3",
            topicId = "top_bd_5",
            question = "¿En qué momentos o eventos DML puede dispararse automáticamente un Trigger en Oracle?",
            optionA = "BEFORE o AFTER de operaciones INSERT, UPDATE o DELETE",
            optionB = "Únicamente cuando se apaga la computadora",
            optionC = "Solo los fines de semana a medianoche",
            optionD = "Cuando se instala un programa en Windows",
            correctOptionIndex = 0,
            explanation = "Los triggers se ejecutan automáticamente antes (BEFORE) o después (AFTER) de modificar datos en una tabla con INSERT, UPDATE o DELETE."
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
        // top_est_1: Python y Pandas para Estadística
        QuizQuestionEntity(
            id = "q_est_1_1",
            topicId = "top_est_1",
            question = "¿Qué método de Pandas se utiliza en una columna categórica para obtener el conteo exacto de repeticiones (frecuencias absolutas) de cada categoría?",
            optionA = "df['columna'].value_counts()",
            optionB = "df['columna'].count_unique()",
            optionC = "df['columna'].sum_values()",
            optionD = "df['columna'].freq()",
            correctOptionIndex = 0,
            explanation = "'.value_counts()' cuenta y ordena automáticamente de mayor a menor las frecuencias de cada categoría en una Serie de Pandas."
        ),
        QuizQuestionEntity(
            id = "q_est_1_2",
            topicId = "top_est_1",
            question = "¿Qué información devuelve por defecto el método 'df.describe()' sobre las columnas numéricas de un DataFrame?",
            optionA = "Conteo, media, desviación estándar, valor mínimo, percentiles 25% (Q1), 50% (mediana), 75% (Q3) y valor máximo",
            optionB = "Solo la suma total y el promedio",
            optionC = "Los nombres de las columnas y sus tipos de datos en memoria",
            optionD = "Una lista de valores nulos ordenados alfabéticamente",
            correctOptionIndex = 0,
            explanation = "'.describe()' genera de forma instantánea el resumen de los 8 principales estadísticos descriptivos de todas las variables numéricas."
        ),
        QuizQuestionEntity(
            id = "q_est_1_3",
            topicId = "top_est_1",
            question = "Para calcular el promedio de sueldos agrupado por cada departamento en un DataFrame 'df', ¿cuál es la sintaxis correcta en Pandas?",
            optionA = "df.groupby('departamento')['sueldo'].mean()",
            optionB = "df.filter('departamento').average('sueldo')",
            optionC = "df.split('departamento').mean()",
            optionD = "df.aggregate('sueldo', by='departamento')",
            correctOptionIndex = 0,
            explanation = "'.groupby('columna_agrupadora')['columna_a_calcular'].mean()' agrupa las filas y aplica la función estadística seleccionada."
        ),
        QuizQuestionEntity(
            id = "q_est_1_4",
            topicId = "top_est_1",
            question = "¿Cómo se seleccionan en Pandas todas las filas donde la variable 'edad' sea mayor o igual a 18 en un DataFrame 'df'?",
            optionA = "df[df['edad'] >= 18]",
            optionB = "df.where('edad >= 18')",
            optionC = "df.filter(edad >= 18)",
            optionD = "df.select('edad >= 18')",
            correctOptionIndex = 0,
            explanation = "La indexación booleana 'df[condicion]' filtra y retorna únicamente las filas que cumplen la condición como True."
        ),
        QuizQuestionEntity(
            id = "q_est_1_5",
            topicId = "top_est_1",
            question = "¿Qué instrucción permite conocer la cantidad de datos faltantes (nulos) por cada columna en un DataFrame 'df'?",
            optionA = "df.isna().sum()",
            optionB = "df.null_count()",
            optionC = "df.missing()",
            optionD = "df.empty_cells()",
            correctOptionIndex = 0,
            explanation = "'.isna()' (o '.isnull()') retorna una máscara booleana y '.sum()' suma los valores True (1) obteniendo el total de nulos por columna."
        ),
        QuizQuestionEntity(
            id = "q_est_1_6",
            topicId = "top_est_1",
            question = "¿Cuál es la diferencia estructural entre una Serie (Series) y un DataFrame en Pandas?",
            optionA = "Una Serie es una estructura unidimensional (1 sola columna con índice) y un DataFrame es bidimensional tabular (filas y columnas)",
            optionB = "La Serie almacena gráficos y el DataFrame números",
            optionC = "El DataFrame no tiene índices",
            optionD = "Una Serie solo admite números enteros",
            correctOptionIndex = 0,
            explanation = "Una Series es un vector 1D indexado; un DataFrame es una tabla 2D compuesta por múltiples Series alineadas por índice."
        ),

        // top_est_2: Clasificación de Variables y Tablas de Frecuencia
        QuizQuestionEntity(
            id = "q_est_2_1",
            topicId = "top_est_2",
            question = "La variable 'Número de asignaturas reprobadas por un estudiante (0, 1, 2, 3...)' se clasifica como:",
            optionA = "Cuantitativa discreta",
            optionB = "Cuantitativa continua",
            optionC = "Cualitativa ordinal",
            optionD = "Cualitativa nominal",
            correctOptionIndex = 0,
            explanation = "Es cuantitativa porque expresa una cantidad numérica y discreta porque proviene de un conteo de valores enteros sin decimales intermedios."
        ),
        QuizQuestionEntity(
            id = "q_est_2_2",
            topicId = "top_est_2",
            question = "¿Cuál de las siguientes variables es un ejemplo de variable Cualitativa Nominal?",
            optionA = "Estado civil (Soltero, Casado, Viudo, Divorciado)",
            optionB = "Nivel de satisfacción en una encuesta (Bajo, Medio, Alto)",
            optionC = "Estatura de una persona en metros",
            optionD = "Puesto de llegada en una carrera (1°, 2°, 3° lugar)",
            correctOptionIndex = 0,
            explanation = "Es cualitativa nominal porque describe una cualidad/categoría sin ningún orden ni jerarquía intrínseca entre ellas."
        ),
        QuizQuestionEntity(
            id = "q_est_2_3",
            topicId = "top_est_2",
            question = "Si en una muestra de 50 personas, 15 prefieren el turno mañana, ¿cuál es la frecuencia relativa (hi) y porcentual (hi%) de ese grupo?",
            optionA = "hi = 0.30 (30%)",
            optionB = "hi = 0.15 (15%)",
            optionC = "hi = 3.33 (33.3%)",
            optionD = "hi = 0.50 (50%)",
            correctOptionIndex = 0,
            explanation = "La frecuencia relativa es hi = fi / N = 15 / 50 = 0.30, lo que multiplicado por 100 corresponde al 30%."
        ),
        QuizQuestionEntity(
            id = "q_est_2_4",
            topicId = "top_est_2",
            question = "¿Qué representa la 'frecuencia absoluta acumulada' (Fi) de una clase en una tabla de frecuencias?",
            optionA = "La suma de las frecuencias absolutas desde la primera clase hasta la clase actual",
            optionB = "El porcentaje total de la muestra",
            optionC = "El promedio de los datos de esa clase",
            optionD = "El valor máximo registrado en la muestra",
            correctOptionIndex = 0,
            explanation = "Fi acumula el número total de observaciones cuyos valores son menores o iguales al límite superior del intervalo actual."
        ),
        QuizQuestionEntity(
            id = "q_est_2_5",
            topicId = "top_est_2",
            question = "¿Cuál es la diferencia fundamental entre una 'Población' y una 'Muestra'?",
            optionA = "La Población es la totalidad de elementos bajo estudio; la Muestra es un subconjunto representativo seleccionado de la población",
            optionB = "La Muestra siempre tiene más datos que la Población",
            optionC = "Población se usa solo en censos de personas y Muestra para animales",
            optionD = "Son sinónimos intercambiables en estadística",
            correctOptionIndex = 0,
            explanation = "Se analiza una muestra para inferir conclusiones sobre toda la población cuando estudiar la población completa resulta costoso o inviable."
        ),

        // top_est_3: Medidas de Tendencia Central y Posición (Percentiles / Cuartiles)
        QuizQuestionEntity(
            id = "q_est_3_1",
            topicId = "top_est_3",
            question = "En una empresa donde 5 operarios ganan $500.000 y el gerente gana $15.000.000, ¿qué medida representa MEJOR el sueldo típico?",
            optionA = "La Mediana, porque es robusta e inmune al sesgo de valores extremos (outliers)",
            optionB = "La Media aritmética, porque utiliza todos los números en la suma",
            optionC = "La Varianza",
            optionD = "El Rango total",
            correctOptionIndex = 0,
            explanation = "La media ($2.916.666) no representa a casi nadie por culpa del valor atípico ($15M); la mediana ($500.000) refleja con exactitud la tendencia central real."
        ),
        QuizQuestionEntity(
            id = "q_est_3_2",
            topicId = "top_est_3",
            question = "¿A qué percentil equivale exactamente el Segundo Cuartil (Q2) de un conjunto de datos?",
            optionA = "Al Percentil 50 (P50) y coincide con la Mediana",
            optionB = "Al Percentil 25 (P25)",
            optionC = "Al Percentil 75 (P75)",
            optionD = "Al Promedio Aritmético",
            correctOptionIndex = 0,
            explanation = "El segundo cuartil (Q2) divide la distribución ordenada en dos mitades del 50% cada una, coincidiendo siempre con la Mediana (P50)."
        ),
        QuizQuestionEntity(
            id = "q_est_3_3",
            topicId = "top_est_3",
            question = "¿Cómo se calcula el Rango Intercuartílico (IQR) y qué porcentaje central de los datos abarca?",
            optionA = "IQR = Q3 - Q1, y contiene exactamente el 50% central de las observaciones",
            optionB = "IQR = Q3 + Q1, y contiene el 75% de los datos",
            optionC = "IQR = Max - Min, y contiene el 100% de los datos",
            optionD = "IQR = Media - Mediana, y contiene el 25% de los datos",
            correctOptionIndex = 0,
            explanation = "El IQR (Q3 - Q1) mide la dispersión del 50% central de los datos, eliminando la influencia de los valores extremos en los extremos."
        ),
        QuizQuestionEntity(
            id = "q_est_3_4",
            topicId = "top_est_3",
            question = "En la regla de Tukey para diagramas de caja (Boxplot), ¿cuándo se considera un valor como atípico (outlier) superior?",
            optionA = "Cuando su valor es estrictamente mayor que Q3 + 1.5 * IQR",
            optionB = "Cuando supera a la Media + 1",
            optionC = "Cuando es mayor que el Percentil 50",
            optionD = "Cuando es el número más alto de la muestra sin importar su valor",
            correctOptionIndex = 0,
            explanation = "El límite superior del bigote en un boxplot es Q3 + 1.5 * IQR; cualquier dato que supere ese umbral se clasifica y grafica como outlier aislado."
        ),

        // top_est_4: Medidas de Dispersión y Homogeneidad
        QuizQuestionEntity(
            id = "q_est_4_1",
            topicId = "top_est_4",
            question = "¿Por qué la Desviación Estándar (s) es más utilizada para interpretar resultados que la Varianza (s²)?",
            optionA = "Porque se expresa en las MISMAS unidades de medida originales de la variable (al ser la raíz cuadrada de la varianza)",
            optionB = "Porque siempre da un número negativo",
            optionC = "Porque no requiere ordenar los datos",
            optionD = "Porque elimina todos los valores duplicados",
            correctOptionIndex = 0,
            explanation = "La varianza eleva las unidades al cuadrado (ej: metros² o pesos²); la desviación estándar recupera la unidad de medida original lineal."
        ),
        QuizQuestionEntity(
            id = "q_est_4_2",
            topicId = "top_est_4",
            question = "¿Para qué se utiliza el Coeficiente de Variación (CV = (s / x̄) * 100%) en el análisis estadístico?",
            optionA = "Para comparar la variabilidad relativa y homogeneidad entre dos o más grupos con diferentes promedios o unidades de medida",
            optionB = "Para predecir el valor del próximo dato",
            optionC = "Para transformar datos cualitativos en cuantitativos",
            optionD = "Para calcular la probabilidad de una distribución normal",
            correctOptionIndex = 0,
            explanation = "Al ser una medida porcentual y adimensional, el CV permite determinar qué grupo es más homogéneo (menor CV) independientemente de su escala."
        ),
        QuizQuestionEntity(
            id = "q_est_4_3",
            topicId = "top_est_4",
            question = "Al calcular la varianza de una muestra (s²), ¿por qué se divide por (n - 1) en lugar de n?",
            optionA = "Para aplicar la corrección de Bessel y obtener un estimador insesgado de la varianza poblacional (σ²)",
            optionB = "Porque se asume que un dato siempre es erróneo",
            optionC = "Para simplificar el cálculo manual",
            optionD = "Solo se aplica cuando la muestra tiene más de 100 datos",
            correctOptionIndex = 0,
            explanation = "Dividir por n - 1 corrige la subestimación natural que produce calcular la varianza muestral respecto a la media de la muestra."
        ),

        // top_est_5: Representación Gráfica e Interpretación
        QuizQuestionEntity(
            id = "q_est_5_1",
            topicId = "top_est_5",
            question = "¿Cuál es la diferencia fundamental entre un Histograma y un Gráfico de Barras?",
            optionA = "El Histograma representa variables cuantitativas continuas en intervalos contiguos (barras unidas); el Gráfico de Barras representa variables cualitativas o discretas (barras separadas)",
            optionB = "El Histograma solo usa sectores circulares",
            optionC = "El Gráfico de Barras es exclusivo para series de tiempo",
            optionD = "Son idénticos y solo cambia el color de las barras",
            correctOptionIndex = 0,
            explanation = "En el histograma no hay espacio entre barras porque representan rangos numéricos continuos (intervalos de clase)."
        ),
        QuizQuestionEntity(
            id = "q_est_5_2",
            topicId = "top_est_5",
            question = "En un Diagrama de Caja (Boxplot), ¿qué estadístico representa la línea horizontal o vertical que corta la caja por dentro?",
            optionA = "La Mediana (Q2)",
            optionB = "La Media aritmética",
            optionC = "La Moda",
            optionD = "La Desviación Estándar",
            correctOptionIndex = 0,
            explanation = "Los bordes de la caja son Q1 y Q3, y la línea interior representa el 50% de los datos (la Mediana o Q2)."
        ),
        QuizQuestionEntity(
            id = "q_est_5_3",
            topicId = "top_est_5",
            question = "¿Para qué tipo de análisis es especialmente útil un Gráfico de Dispersión (Scatter Plot)?",
            optionA = "Para evaluar visualmente la relación, tendencia y correlación (lineal, no lineal o nula) entre dos variables cuantitativas",
            optionB = "Para mostrar porcentajes de una variable cualitativa",
            optionC = "Para ordenar datos alfabéticamente",
            optionD = "Para calcular la frecuencia acumulada",
            correctOptionIndex = 0,
            explanation = "El gráfico de dispersión grafica pares de datos (X, Y) para identificar si a mayor valor de X aumenta o disminuye Y (correlación)."
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
