package com.example.semfour.data.local

import com.example.semfour.data.local.entity.QuizQuestionEntity

/**
 * Catálogo maestro oficial de micro-lecciones y preguntas de selección múltiple (Active Recall)
 * para los temas de las 6 asignaturas del 4.º Semestre (Duoc UC).
 *
 * Cada pregunta incluye un 'theoryContext' pedagógico que enseña el concepto clave
 * antes de responder para facilitar el aprendizaje directo dentro de la app.
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
            theoryContext = "Kotlin Multiplatform (KMP) permite compartir lógica de negocio (modelos, peticiones HTTP, algoritmos, bases de datos) en código Kotlin 100% nativo entre Android e iOS, mientras permite construir la interfaz de usuario con los frameworks nativos de cada plataforma (Jetpack Compose en Android y SwiftUI en iOS).",
            question = "¿Cuál es la principal ventaja de Kotlin Multiplatform (KMP) frente a frameworks híbridos tradicionales como Flutter o React Native?",
            optionA = "Permite compartir la lógica de negocio en Kotlin compilando a código nativo sin forzar un motor de renderizado propio",
            optionB = "Obliga a compilar todo a JavaScript para ejecutar dentro de un WebView",
            optionC = "Solo funciona en dispositivos Android y no puede ejecutarse en iOS",
            optionD = "Reemplaza completamente a Swift y no permite usar APIs de Apple",
            correctOptionIndex = 0,
            explanation = "KMP no dibuja los píxeles con un motor propio, sino que compila la lógica a binario nativo (Objective-C/Swift framework en iOS y JVM/DEX en Android), permitiendo UI 100% nativa."
        ),
        QuizQuestionEntity(
            id = "q_mov_1_2",
            topicId = "top_mov_1",
            theoryContext = "En el Google I/O 2019, Google anunció formalmente que el desarrollo de Android pasaba a ser 'Kotlin-First', convirtiendo a Kotlin en el lenguaje preferido y prioritario para todas las nuevas herramientas, APIs de Jetpack y documentación oficial.",
            question = "¿Qué lenguaje es recomendado oficialmente por Google como prioritario (Kotlin-First) para el desarrollo nativo en Android?",
            optionA = "Java",
            optionB = "Kotlin",
            optionC = "Dart",
            optionD = "C++",
            correctOptionIndex = 1,
            explanation = "Kotlin es el lenguaje prioritario de Android debido a su seguridad de nulos, sintaxis concisa, corrutinas y compatibilidad total con Jetpack Compose."
        ),
        QuizQuestionEntity(
            id = "q_mov_1_3",
            topicId = "top_mov_1",
            theoryContext = "El desarrollo Nativo utiliza los SDKs oficiales de Google (Android SDK/Kotlin) y Apple (iOS SDK/Swift), otorgando acceso sin capas intermedias al 100% de las funciones del hardware (cámara, sensores, Bluetooth, GPU) con el máximo rendimiento y fluidez posible a 60/120 FPS.",
            question = "¿En qué se diferencia el desarrollo móvil Nativo del desarrollo Web o Híbrido?",
            optionA = "El nativo se programa con las herramientas y lenguajes oficiales del fabricante accediendo directamente al hardware con máximo rendimiento",
            optionB = "El nativo no puede acceder a la cámara ni al GPS del teléfono",
            optionC = "El desarrollo híbrido siempre es más rápido y fluido que el nativo",
            optionD = "El nativo requiere ejecutar un navegador web dentro de la app",
            correctOptionIndex = 0,
            explanation = "El código nativo se ejecuta directamente sobre el sistema operativo móvil sin puentes de comunicación adicionales ni navegadores embebidos."
        ),

        // top_mov_2: Fundamentos de Kotlin
        QuizQuestionEntity(
            id = "q_mov_2_1",
            topicId = "top_mov_2",
            theoryContext = "En Kotlin, la inmutabilidad es una buena práctica recomendada. 'val' declara una variable de solo lectura (inmutable) que no puede reasignarse tras su inicialización (similar a 'final' en Java). 'var' declara una variable mutable cuyo valor puede cambiarse en cualquier momento.",
            question = "¿Cuál es la diferencia entre 'val' y 'var' en Kotlin?",
            optionA = "'val' es de solo lectura (inmutable tras asignarse) y 'var' es mutable y reasignable",
            optionB = "'val' solo almacena texto y 'var' solo números",
            optionC = "'val' es mutable y 'var' inmutable",
            optionD = "No hay diferencia, son sinónimos",
            correctOptionIndex = 0,
            explanation = "Se recomienda usar 'val' por defecto para prevenir efectos secundarios y cambios inesperados de estado."
        ),
        QuizQuestionEntity(
            id = "q_mov_2_2",
            topicId = "top_mov_2",
            theoryContext = "El sistema de tipos de Kotlin previene el temido NullPointerException. El operador Elvis '?:' evalúa la expresión a la izquierda: si no es nula la devuelve; si es nula, ejecuta o devuelve el valor de respaldo a la derecha (ej: val nombre = input ?: 'Invitado').",
            question = "¿Qué operador en Kotlin se conoce como 'Elvis operator' para proporcionar un valor por defecto si una variable es nula?",
            optionA = "!!",
            optionB = "?:",
            optionC = "?.",
            optionD = "as?",
            correctOptionIndex = 1,
            explanation = "El operador '?:' se llama Elvis porque se asemeja a los ojos y el peinado de Elvis Presley, y asigna un valor por defecto cuando hay un null."
        ),
        QuizQuestionEntity(
            id = "q_mov_2_3",
            topicId = "top_mov_2",
            theoryContext = "En Kotlin, estructuras como 'if' y 'when' no son solo sentencias de control de flujo, sino que son 'expresiones'. Esto significa que pueden devolver un valor y asignarse directamente a una variable (ej: val mensaje = if (edad >= 18) 'Mayor' else 'Menor').",
            question = "¿Cómo se comporta la estructura 'when' e 'if' en Kotlin a diferencia de Java tradicional?",
            optionA = "Pueden utilizarse como expresiones que retornan un valor directamente para asignarlo a una variable",
            optionB = "Solo pueden ejecutarse como sentencias sin retornar nada",
            optionC = "'when' requiere obligatoriamente colocar la palabra 'break' en cada caso",
            optionD = "No admiten ramas 'else'",
            correctOptionIndex = 0,
            explanation = "Al ser expresiones, reducen la necesidad de declarar variables mutables antes de evaluar la condición."
        ),
        QuizQuestionEntity(
            id = "q_mov_2_4",
            topicId = "top_mov_2",
            theoryContext = "Kotlin cuenta con inferencia de tipos: el compilador deduce automáticamente el tipo de dato analizando el valor asignado (ej: val x = 42 deduce que x es de tipo Int), lo que ahorra escribir tipos repetitivos.",
            question = "¿Qué significa que Kotlin tenga 'Inferencia de Tipos'?",
            optionA = "Que el compilador detecta automáticamente el tipo de dato de la variable sin obligar a escribir 'Int', 'String', etc. explícitamente",
            optionB = "Que las variables cambian de tipo automáticamente en tiempo de ejecución",
            optionC = "Que todos los datos se convierten en texto",
            optionD = "Que el código no tiene tipado estático",
            correctOptionIndex = 0,
            explanation = "Kotlin es un lenguaje fuertemente tipado en tiempo de compilación, pero su inferencia evita código redundante."
        ),

        // top_mov_3: Colecciones y Funciones en Kotlin
        QuizQuestionEntity(
            id = "q_mov_3_1",
            topicId = "top_mov_3",
            theoryContext = "La función de orden superior '.map { ... }' itera sobre cada elemento de una colección, le aplica la función de transformación especificada y retorna una nueva lista con los resultados (ej: listOf(1, 2).map { it * 2 } produce listOf(2, 4)).",
            question = "¿Qué función de colección en Kotlin transforma cada elemento aplicando una función y retorna una nueva lista resultante?",
            optionA = "filter",
            optionB = "map",
            optionC = "forEach",
            optionD = "reduce",
            correctOptionIndex = 1,
            explanation = "'.map()' transforma datos elemento a elemento sin alterar la colección original."
        ),
        QuizQuestionEntity(
            id = "q_mov_3_2",
            topicId = "top_mov_3",
            theoryContext = "En Kotlin las colecciones son inmutables por defecto. 'listOf()' genera una List de solo lectura (no tiene métodos .add ni .remove), mientras que 'mutableListOf()' genera una MutableList que permite modificar su tamaño y elementos.",
            question = "¿Cuál es la diferencia entre 'listOf()' y 'mutableListOf()' en Kotlin?",
            optionA = "'listOf()' produce una lista inmutable (solo lectura), mientras que 'mutableListOf()' permite agregar/quitar elementos",
            optionB = "'listOf()' es sincrónica y 'mutableListOf()' asincrónica",
            optionC = "Ambas son mutables pero tienen diferente rendimiento",
            optionD = "'listOf()' solo acepta cadenas de texto",
            correctOptionIndex = 0,
            explanation = "Separar interfaces de lectura e interfaces mutables garantiza seguridad en concurrencia y previene modificaciones accidentales."
        ),
        QuizQuestionEntity(
            id = "q_mov_3_3",
            topicId = "top_mov_3",
            theoryContext = "La función '.filter { ... }' evalúa un predicado booleano en cada elemento y devuelve una nueva lista que contiene únicamente los elementos que cumplieron la condición (ej: numeros.filter { it % 2 == 0 } obtiene los pares).",
            question = "¿Qué operación realiza la función '.filter { ... }' sobre una colección?",
            optionA = "Conserva únicamente los elementos que cumplen la condición booleana indicada",
            optionB = "Ordena los elementos de mayor a menor",
            optionC = "Multiplica los elementos por un factor",
            optionD = "Elimina la lista completa de la memoria",
            correctOptionIndex = 0,
            explanation = "'.filter' filtra datos dejando pasar solo aquellos para los cuales la lambda devuelve true."
        ),

        // top_mov_4: POO y Control de Errores en Kotlin
        QuizQuestionEntity(
            id = "q_mov_4_1",
            topicId = "top_mov_4",
            theoryContext = "En Kotlin, todas las clases y métodos son 'final' por defecto (cerradas a la herencia). Para permitir que otra clase herede de una clase padre, dicha clase debe declararse explícitamente con la palabra clave 'open' (ej: open class Vehiculo).",
            question = "¿Qué palabra clave es obligatoria en Kotlin sobre una clase para permitir que otras clases hereden de ella?",
            optionA = "open",
            optionB = "public",
            optionC = "abstract",
            optionD = "extendable",
            correctOptionIndex = 0,
            explanation = "El principio 'Effective Java' de favorecer composición sobre herencia se aplica por defecto en Kotlin haciendo las clases cerradas salvo que lleven 'open'."
        ),
        QuizQuestionEntity(
            id = "q_mov_4_2",
            topicId = "top_mov_4",
            theoryContext = "La clase 'Result<T>' en Kotlin encapsula el resultado exitoso ('Result.success(valor)') o el error ('Result.failure(excepcion)') de una operación, permitiendo manejar fallos de forma funcional y explícita sin que la aplicación se caiga.",
            question = "¿Qué ventaja ofrece el uso de la clase 'Result' (Result.success / Result.failure) frente al try-catch tradicional?",
            optionA = "Permite modelar fallos esperados de forma funcional y explícita en el retorno del método sin interrumpir el flujo",
            optionB = "Acelera el cálculo de operaciones matemáticas complejas",
            optionC = "Evita tener que declarar variables inmutables",
            optionD = "Convierte automáticamente código Kotlin a SQL",
            correctOptionIndex = 0,
            explanation = "'Result' es idónea para respuestas de red, APIs o validaciones de formulario donde el fallo es un escenario habitual y previsible."
        ),
        QuizQuestionEntity(
            id = "q_mov_4_3",
            topicId = "top_mov_4",
            theoryContext = "Kotlin introduce el modificador de visibilidad 'internal'. Una clase, función o propiedad marcada como 'internal' es accesible desde cualquier archivo dentro del mismo módulo de compilación (ej. módulo de Gradle), pero está oculta para otros módulos.",
            question = "¿Qué modificador de visibilidad en Kotlin restringe el acceso para que SOLO sea visible dentro del mismo módulo de compilación?",
            optionA = "internal",
            optionB = "private",
            optionC = "protected",
            optionD = "package-private",
            correctOptionIndex = 0,
            explanation = "'internal' ayuda a encapsular la arquitectura interna de bibliotecas y módulos de Gradle sin exponer detalles a consumidores externos."
        ),

        // top_mov_5: Corrutinas y Sintaxis Avanzada
        QuizQuestionEntity(
            id = "q_mov_5_1",
            topicId = "top_mov_5",
            theoryContext = "Las corrutinas son hilos ligeros para asincronía. 'launch' crea una corrutina para tareas 'fire-and-forget' sin retornar valor (devuelve un Job). 'async' se usa cuando necesitas un resultado en el futuro (retorna un Deferred<T>) que se recupera con '.await()'.",
            question = "¿Cuál es la diferencia entre los constructores de corrutinas 'launch' y 'async' en Kotlin?",
            optionA = "'launch' inicia una corrutina sin retornar resultado directo; 'async' retorna un Deferred<T> cuyo resultado se obtiene con .await()",
            optionB = "'launch' solo funciona en el hilo principal y 'async' en segundo plano",
            optionC = "'async' no puede pausarse con suspend",
            optionD = "Son idénticos pero 'launch' es exclusivo para Java",
            correctOptionIndex = 0,
            explanation = "'launch' se usa para operaciones como guardar en base de datos o analytics; 'async' cuando necesitas combinar datos de múltiples APIs en paralelo."
        ),
        QuizQuestionEntity(
            id = "q_mov_5_2",
            topicId = "top_mov_5",
            theoryContext = "Las 'Sealed Classes' representan jerarquías de clases cerradas donde todas las subclases son conocidas en tiempo de compilación. Son el estándar para modelar estados de interfaz gráfica (UI State: Loading, Success(data), Error(msg)) evaluables en un 'when' sin requerir 'else'.",
            question = "¿Para qué se utilizan principalmente las 'Sealed Classes' (clases selladas) en aplicaciones móviles modernas?",
            optionA = "Para modelar estados finitos y exhaustivos de la UI (Loading, Success, Error) manejables de forma segura en un 'when'",
            optionB = "Para encriptar la base de datos local SQLite",
            optionC = "Para gestionar conexiones Bluetooth",
            optionD = "Para forzar la recarga de imágenes en segundo plano",
            correctOptionIndex = 0,
            explanation = "Si agregas un nuevo estado a una sealed class, el compilador te avisará en todos los 'when' donde falte contemplarlo."
        ),
        QuizQuestionEntity(
            id = "q_mov_5_3",
            topicId = "top_mov_5",
            theoryContext = "Una 'data class' en Kotlin está diseñada para almacenar datos. El compilador genera automáticamente los métodos equals(), hashCode(), toString(), copy() y componentN() en base a las propiedades declaradas en su constructor primario.",
            question = "¿Qué genera automáticamente el compilador al declarar una 'data class' en Kotlin?",
            optionA = "equals(), hashCode(), toString(), copy() y funciones componentN() para desestructuración",
            optionB = "Una interfaz gráfica en Compose automáticamente",
            optionC = "Una base de datos SQLite remota",
            optionD = "Hilos asíncronos en segundo plano",
            correctOptionIndex = 0,
            explanation = "Las data classes eliminan cientos de líneas de código repetitivo de clases POJO/Bean de Java."
        ),

        // top_mov_6: Android Studio y Jetpack Compose
        QuizQuestionEntity(
            id = "q_mov_6_1",
            topicId = "top_mov_6",
            theoryContext = "Jetpack Compose es el toolkit moderno para UI nativa de Android basado en el paradigma Declarativo: en lugar de mutar vistas imperativamente (findViewById), describes cómo debe verse la pantalla en función del estado actual y Compose se recompone automáticamente.",
            question = "¿Cuál es el paradigma fundamental de Jetpack Compose en comparación con el sistema tradicional de Views/XML?",
            optionA = "UI Declarativa donde la interfaz se describe en función del estado y se recompone automáticamente al haber cambios",
            optionB = "Programación Imperativa orientada a objetos con findViewById",
            optionC = "Compilación exclusiva a HTML y JavaScript",
            optionD = "Manipulación manual de árboles DOM",
            correctOptionIndex = 0,
            explanation = "Al separar el estado de la representación visual, se previenen inconsistencias visuales y se reduce drásticamente el código de UI."
        ),

        // ══════════════════════════════════════════════════════════════════════════
        // DESARROLLO ORIENTADO A OBJETOS (DSY1102)
        // ══════════════════════════════════════════════════════════════════════════
        // top_poo_1: Paradigma POO y Abstracción
        QuizQuestionEntity(
            id = "q_poo_1_1",
            topicId = "top_poo_1",
            theoryContext = "La Abstracción es el pilar de la POO que consiste en identificar y modelar únicamente las características (atributos) y comportamientos (métodos) esenciales de una entidad del mundo real para el problema a resolver, ignorando detalles irrelevantes (ej. en una biblioteca, registrar título e ISBN de un libro, ignorando el grosor de sus hojas).",
            question = "¿En qué consiste el principio de 'Abstracción' al modelar una clase a partir de un caso del mundo real?",
            optionA = "Identificar y capturar únicamente los atributos y acciones esenciales para el problema a resolver, omitiendo detalles irrelevantes",
            optionB = "Copiar todos los detalles físicos de los objetos reales sin excluir nada",
            optionC = "Crear únicamente métodos estáticos sin atributos",
            optionD = "Convertir el código Java a lenguaje de máquina directamente",
            correctOptionIndex = 0,
            explanation = "La abstracción permite crear modelos simples, enfocados y funcionales sin saturar el sistema con datos innecesarios."
        ),
        QuizQuestionEntity(
            id = "q_poo_1_2",
            topicId = "top_poo_1",
            theoryContext = "Una Clase es el plano, plantilla o molde que define qué atributos y métodos tendrán las entidades. Un Objeto es una instancia concreta creada en memoria en tiempo de ejecución a partir de esa clase mediante el operador 'new' (ej. Clase: Auto, Objeto: miAutoRojo).",
            question = "¿Cuál es la relación fundamental entre una 'Clase' y un 'Objeto' en Java?",
            optionA = "La Clase es la plantilla/molde que define la estructura y el Objeto es una instancia concreta creada en memoria",
            optionB = "El Objeto es la plantilla y la Clase es la instancia",
            optionC = "Son exactamente lo mismo sin ninguna distinción técnica",
            optionD = "Una clase solo puede tener una única instancia en toda la aplicación",
            correctOptionIndex = 0,
            explanation = "A partir de una sola clase se pueden instanciar infinitos objetos con distintos estados en memoria."
        ),
        QuizQuestionEntity(
            id = "q_poo_1_3",
            topicId = "top_poo_1",
            theoryContext = "Los 4 pilares fundamentales de la Programación Orientada a Objetos son: Abstracción (modelar lo esencial), Encapsulamiento (proteger el estado interno), Herencia (reutilizar y jerarquizar) y Polimorfismo (múltiples formas de responder a un mensaje).",
            question = "¿Cuál de los siguientes NO es uno de los 4 pilares fundamentales de la Programación Orientada a Objetos?",
            optionA = "Encapsulamiento",
            optionB = "Herencia",
            optionC = "Polimorfismo",
            optionD = "Recursividad",
            correctOptionIndex = 3,
            explanation = "La recursividad es una técnica algorítmica de programación general, no un pilar específico del paradigma orientado a objetos."
        ),

        // top_poo_2: Tipos, Estructuras de Control y Métodos
        QuizQuestionEntity(
            id = "q_poo_2_1",
            topicId = "top_poo_2",
            theoryContext = "En Java, el paso de parámetros a métodos siempre se realiza por valor. Para los tipos primitivos (byte, short, int, long, float, double, char, boolean), se pasa una copia exacta del valor numérico, por lo que modificar el parámetro dentro del método no afecta a la variable original.",
            question = "¿Qué sucede en Java cuando se invoca un método pasándole parámetros de tipo primitivo (ej: int, double)?",
            optionA = "Se pasan estrictamente por valor (se genera una copia local del dato dentro del método)",
            optionB = "Se pasan por referencia directa a la memoria original",
            optionC = "Se convierten automáticamente a un String",
            optionD = "El método no puede modificarlos ni utilizarlos",
            correctOptionIndex = 0,
            explanation = "Cualquier reasignación a un parámetro primitivo solo vive en el alcance (stack) del método invocado."
        ),
        QuizQuestionEntity(
            id = "q_poo_2_2",
            topicId = "top_poo_2",
            theoryContext = "Un bucle 'while' evalúa la condición al inicio (si es falsa, puede no ejecutarse nunca). Un bucle 'do-while' tiene la condición al final, garantizando que el bloque de código se ejecute al menos una vez obligatoriamente antes de verificar la condición.",
            question = "¿Cuál es la diferencia principal entre un ciclo 'while' y un ciclo 'do-while' en Java?",
            optionA = "El ciclo 'do-while' ejecuta su bloque al menos una vez antes de evaluar la condición, mientras que 'while' evalúa la condición al inicio",
            optionB = "'while' solo admite números enteros y 'do-while' cadenas",
            optionC = "'do-while' no permite utilizar la instrucción 'break'",
            optionD = "Son idénticos y el compilador los transforma en la misma instrucción",
            correctOptionIndex = 0,
            explanation = "'do-while' es perfecto para menús de consola donde primero se debe mostrar el menú y luego evaluar la opción seleccionada."
        ),
        QuizQuestionEntity(
            id = "q_poo_2_3",
            topicId = "top_poo_2",
            theoryContext = "La Sobrecarga de Métodos (Method Overloading) permite definir dentro de la misma clase múltiples métodos con el mismo nombre, siempre y cuando difieran en el número, orden o tipo de sus parámetros.",
            question = "¿Qué es la 'Sobrecarga de Métodos' (Method Overloading) en Java?",
            optionA = "Definir múltiples métodos en una misma clase con el mismo nombre pero diferente lista o tipo de parámetros",
            optionB = "Reescribir un método de la clase padre en una subclase",
            optionC = "Llenar la memoria RAM con demasiadas llamadas a funciones",
            optionD = "Un error de compilación por duplicidad de nombres",
            correctOptionIndex = 0,
            explanation = "La sobrecarga ofrece versatilidad para invocar una acción con distintos argumentos (ej: calcularArea(int lado) vs calcularArea(int base, int altura))."
        ),

        // top_poo_3: Clases, Constructores y Encapsulamiento
        QuizQuestionEntity(
            id = "q_poo_3_1",
            topicId = "top_poo_3",
            theoryContext = "La palabra clave 'this' en Java es una referencia a la instancia actual del objeto. Se utiliza comúnmente en constructores y setters para resolver ambigüedades entre los nombres de los atributos de la clase y los parámetros recibidos (ej: this.nombre = nombre;).",
            question = "¿Para qué se utiliza la palabra clave 'this' en un constructor o método de Java?",
            optionA = "Para referenciar explícitamente los atributos o métodos de la instancia actual y distinguirlos de los parámetros con igual nombre",
            optionB = "Para importar librerías externas",
            optionC = "Para pausar el hilo de ejecución",
            optionD = "Para destruir el objeto en memoria",
            correctOptionIndex = 0,
            explanation = "'this' apunta al objeto que está ejecutando el código en ese instante."
        ),
        QuizQuestionEntity(
            id = "q_poo_3_2",
            topicId = "top_poo_3",
            theoryContext = "El Encapsulamiento busca proteger la integridad de los datos de un objeto ocultando sus atributos con el modificador 'private' y proveyendo métodos públicos 'getters' (lectura) y 'setters' (escritura con validaciones) para controlar el acceso.",
            question = "¿Qué modificador de acceso restringe la visibilidad de un atributo para que SOLO sea accesible dentro de su propia clase?",
            optionA = "private",
            optionB = "public",
            optionC = "protected",
            optionD = "default (package-private)",
            correctOptionIndex = 0,
            explanation = "'private' es el nivel de encapsulamiento más estricto, impidiendo modificaciones externas no controladas."
        ),

        // top_poo_4: Herencia y Polimorfismo
        QuizQuestionEntity(
            id = "q_poo_4_1",
            topicId = "top_poo_4",
            theoryContext = "En Java, una subclase utiliza la palabra reservada 'super()' para llamar al constructor de su clase padre (superclase). Esta llamada debe ser obligatoriamente la primera línea de código dentro del constructor de la subclase.",
            question = "¿Cómo invoca una subclase en Java al constructor de su clase padre?",
            optionA = "Mediante la llamada 'super(parametros);' como primera línea del constructor",
            optionB = "Llamando a 'this(parametros);'",
            optionC = "Con el comando 'Parent.create()'",
            optionD = "No se puede invocar el constructor padre en Java",
            correctOptionIndex = 0,
            explanation = "'super()' garantiza que el estado heredado de la superclase quede correctamente inicializado antes de ejecutar la lógica de la subclase."
        ),
        QuizQuestionEntity(
            id = "q_poo_4_2",
            topicId = "top_poo_4",
            theoryContext = "El Polimorfismo permite que una referencia de tipo padre (ej: Animal a = new Perro()) responda de manera específica ejecutando el método sobrescrito de la clase hija. La anotación '@Override' asegura que la firma coincida exactamente con el método de la superclase.",
            question = "¿Qué anotación se recomienda colocar en Java sobre un método que sobrescribe el comportamiento de una clase base?",
            optionA = "@Override",
            optionB = "@Overload",
            optionC = "@Inherited",
            optionD = "@Replace",
            correctOptionIndex = 0,
            explanation = "@Override permite al compilador alertar si cometiste un error tipográfico en el nombre o parámetros del método sobrescrito."
        ),

        // top_poo_5: Clases Abstractas e Interfaces
        QuizQuestionEntity(
            id = "q_poo_5_1",
            topicId = "top_poo_5",
            theoryContext = "En Java, una clase solo puede heredar de una única clase ('extends'), pero puede implementar múltiples interfaces ('implements'). Las interfaces actúan como contratos de comportamiento que garantizan qué métodos debe implementar una clase.",
            question = "¿Cuál es una diferencia clave entre una 'clase abstracta' y una 'interfaz' en Java?",
            optionA = "Una clase solo puede heredar de una clase abstracta ('extends'), pero puede implementar múltiples interfaces ('implements')",
            optionB = "Las interfaces pueden tener constructores públicos y las abstractas no",
            optionC = "Una clase abstracta no puede contener ningún método implementado",
            optionD = "Las interfaces solo pueden ser privadas",
            correctOptionIndex = 0,
            explanation = "La implementación múltiple de interfaces permite simular herencia múltiple de comportamiento en Java de forma segura."
        ),

        // top_poo_6: Colecciones y Excepciones
        QuizQuestionEntity(
            id = "q_poo_6_1",
            topicId = "top_poo_6",
            theoryContext = "En el framework de colecciones de Java, 'ArrayList' es una lista secuencial indexada que mantiene el orden de inserción y permite duplicados. 'HashSet' implementa un conjunto matemático que no mantiene orden y no permite ningún elemento duplicado.",
            question = "¿Cuál es la principal diferencia entre un 'ArrayList' y un 'HashSet' en Java?",
            optionA = "ArrayList mantiene orden de inserción y permite duplicados; HashSet no garantiza orden y NO permite elementos duplicados",
            optionB = "ArrayList solo acepta enteros y HashSet solo cadenas",
            optionC = "HashSet permite duplicados y ArrayList no",
            optionD = "No hay diferencia funcional",
            correctOptionIndex = 0,
            explanation = "'HashSet' utiliza las funciones hashCode() y equals() para garantizar unicidad y búsquedas en tiempo O(1)."
        ),
        QuizQuestionEntity(
            id = "q_poo_6_2",
            topicId = "top_poo_6",
            theoryContext = "En el control de excepciones con 'try-catch-finally', el bloque 'finally' se ejecuta SIEMPRE de forma incondicional, ocurra o no una excepción en el 'try'. Por esta razón, es el lugar adecuado para cerrar conexiones a bases de datos o archivos.",
            question = "En la estructura 'try-catch-finally' de Java, ¿cuándo se ejecuta el bloque 'finally'?",
            optionA = "Siempre se ejecuta, haya ocurrido o no una excepción en el bloque try",
            optionB = "Solo si ocurrió un error en el catch",
            optionC = "Únicamente si la excepción es de tipo NullPointerException",
            optionD = "Solo si no se lanza ninguna excepción",
            correctOptionIndex = 0,
            explanation = "'finally' garantiza la limpieza de recursos y liberación de memoria del sistema."
        ),

        // ══════════════════════════════════════════════════════════════════════════
        // TALLER DE BASE DE DATOS (BDY1103 - PL/SQL ORACLE)
        // ══════════════════════════════════════════════════════════════════════════
        // top_bd_1: Estructuras Compuestas (RECORD y VARRAY)
        QuizQuestionEntity(
            id = "q_bd_1_1",
            topicId = "top_bd_1",
            theoryContext = "En Oracle PL/SQL, el atributo '%ROWTYPE' se utiliza para declarar un registro (RECORD) que hereda automáticamente la estructura completa de columnas y tipos de datos de una tabla o vista (ej: emp_rec emp%ROWTYPE).",
            question = "¿Qué directiva se utiliza en PL/SQL para declarar una variable con la estructura completa de una fila de tabla?",
            optionA = "%ROWTYPE",
            optionB = "%TYPE",
            optionC = "%RECORD",
            optionD = "%VARRAY",
            correctOptionIndex = 0,
            explanation = "'%ROWTYPE' facilita el mantenimiento: si la tabla agrega una nueva columna, el bloque PL/SQL la adopta sin reescribir variables."
        ),
        QuizQuestionEntity(
            id = "q_bd_1_2",
            topicId = "top_bd_1",
            theoryContext = "Un 'VARRAY' (Variable-Size Array) en PL/SQL es una colección homogénea en memoria que almacena un número fijo y acotado de elementos del mismo tipo, definido al declarar el tipo (ej: TYPE TelVarray IS VARRAY(5) OF VARCHAR2(15)).",
            question = "¿Cuáles son las características principales del tipo de dato 'VARRAY' en PL/SQL?",
            optionA = "Almacena una colección en memoria con elementos del mismo tipo y un número máximo de elementos fijo definido en su declaración",
            optionB = "Es una tabla temporal que no admite números",
            optionC = "Puede crecer indefinidamente sin ningún límite establecido",
            optionD = "Solo se puede usar dentro de funciones Java",
            correctOptionIndex = 0,
            explanation = "Los elementos de un VARRAY se indexan secuencialmente a partir del índice 1 y conservan su orden en memoria."
        ),
        QuizQuestionEntity(
            id = "q_bd_1_3",
            topicId = "top_bd_1",
            theoryContext = "Para interactuar con los campos de una variable de tipo RECORD en PL/SQL, se utiliza la notación de punto estándar (nombre_registro.nombre_campo), permitiendo leer o asignar valores de forma individual (ej: v_dept.dname := 'VENTAS';).",
            question = "¿Cómo se accede o asigna un valor a un campo individual dentro de una variable de tipo RECORD en PL/SQL?",
            optionA = "Mediante la notación de punto: variable_record.nombre_campo",
            optionB = "Con corchetes: variable_record['campo']",
            optionC = "Con una flecha: variable_record->campo",
            optionD = "No se puede acceder individualmente",
            correctOptionIndex = 0,
            explanation = "La notación por punto es idéntica a la manipulación de propiedades en lenguajes de alto nivel."
        ),

        // top_bd_2: Cursores Complejos y con Parámetros
        QuizQuestionEntity(
            id = "q_bd_2_1",
            topicId = "top_bd_2",
            theoryContext = "Los cursores explícitos con parámetros en PL/SQL permiten definir consultas reutilizables donde los criterios del WHERE se pasan dinámicamente al abrir el cursor (ej: CURSOR c_emp(p_dept NUMBER) IS SELECT * FROM emp WHERE deptno = p_dept;).",
            question = "¿Cuál es la principal ventaja de utilizar cursores explícitos con parámetros en PL/SQL?",
            optionA = "Permiten reutilizar la misma consulta pasándole diferentes valores para seleccionar datos dinámicamente según criterios variables",
            optionB = "Hacen que la base de datos ignore las claves foráneas",
            optionC = "Evitan tener que declarar tipos de datos en la base",
            optionD = "Convierten la consulta en una tabla física",
            correctOptionIndex = 0,
            explanation = "Evitan tener que duplicar múltiples cursores para consultar diferentes departamentos o rangos salariales."
        ),
        QuizQuestionEntity(
            id = "q_bd_2_2",
            topicId = "top_bd_2",
            theoryContext = "Cuando se abre un cursor explícito con 'OPEN', Oracle reserva memoria privada en el servidor (área de contexto). Si no se cierra con 'CLOSE', la memoria queda retenida, pudiendo alcanzar el límite 'ORA-01000: maximum open cursors exceeded'.",
            question = "¿Por qué es fundamental cerrar un cursor explícito abierto manualmente mediante 'CLOSE'?",
            optionA = "Para liberar la memoria del área de contexto y los recursos asignados en el servidor Oracle",
            optionB = "Para que la tabla no se borre del disco",
            optionC = "Para que los datos se guarden permanentemente en la nube",
            optionD = "No es necesario cerrar cursores explícitos",
            correctOptionIndex = 0,
            explanation = "Cerrar los cursores asegura la escalabilidad y disponibilidad de recursos en el servidor de base de datos."
        ),
        QuizQuestionEntity(
            id = "q_bd_2_3",
            topicId = "top_bd_2",
            theoryContext = "El bucle 'FOR r IN nombre_cursor LOOP' es una estructura avanzada de PL/SQL que abre el cursor (OPEN), declara implícitamente el registro 'r %ROWTYPE', extrae las filas (FETCH) una a una y cierra el cursor (CLOSE) automáticamente al terminar.",
            question = "¿Qué estructura de control simplifica el manejo de un cursor abriéndolo, extrayendo las filas (fetch) y cerrándolo automáticamente?",
            optionA = "El ciclo FOR registro IN nombre_cursor LOOP ... END LOOP;",
            optionB = "El ciclo WHILE (cursor == open)",
            optionC = "La sentencia IF MATCH",
            optionD = "El comando TRY-CATCH",
            correctOptionIndex = 0,
            explanation = "El Cursor FOR LOOP previene fugas de memoria al garantizar el cierre automático del cursor incluso si se sale antes del bucle."
        ),

        // top_bd_3: Manejo de Excepciones Predefinidas y Definidas por el Usuario
        QuizQuestionEntity(
            id = "q_bd_3_1",
            topicId = "top_bd_3",
            theoryContext = "En Oracle PL/SQL, una sentencia 'SELECT INTO' espera recuperar exactamente una fila. Si la consulta no encuentra ningún registro coincidente en la tabla, el motor dispara automáticamente la excepción predefinida 'NO_DATA_FOUND'.",
            question = "¿Qué excepción predefinida se dispara cuando una consulta 'SELECT INTO' en PL/SQL no encuentra ningún registro coincidente?",
            optionA = "NO_DATA_FOUND",
            optionB = "TOO_MANY_ROWS",
            optionC = "ZERO_DIVIDE",
            optionD = "INVALID_CURSOR",
            correctOptionIndex = 0,
            explanation = "Capturar 'WHEN NO_DATA_FOUND THEN' permite desplegar un mensaje amigable o asignar valores por defecto en lugar de abortar el programa."
        ),
        QuizQuestionEntity(
            id = "q_bd_3_2",
            topicId = "top_bd_3",
            theoryContext = "Para controlar reglas de negocio (ej. 'saldo insuficiente'), el desarrollador puede declarar una variable de tipo EXCEPTION y lanzarla con la sentencia 'RAISE nombre_excepcion;' o invocar el procedimiento estándar 'RAISE_APPLICATION_ERROR(codigo, mensaje)' con códigos entre -20000 y -20999.",
            question = "¿Qué instrucción de PL/SQL permite lanzar manualmente una excepción personalizada creada por el desarrollador?",
            optionA = "RAISE mi_excepcion; o RAISE_APPLICATION_ERROR(-20001, 'Mensaje');",
            optionB = "THROW mi_excepcion;",
            optionC = "CATCH mi_excepcion;",
            optionD = "TRY mi_excepcion;",
            correctOptionIndex = 0,
            explanation = "'RAISE_APPLICATION_ERROR' devuelve el mensaje de error personalizado a la aplicación cliente (Java, Python, Web)."
        ),
        QuizQuestionEntity(
            id = "q_bd_3_3",
            topicId = "top_bd_3",
            theoryContext = "Si una consulta 'SELECT INTO' devuelve 2 o más filas, Oracle no puede decidir cuál asignar a las variables escalares y dispara de inmediato la excepción predefinida 'TOO_MANY_ROWS'.",
            question = "¿Qué excepción predefinida ocurre cuando una consulta 'SELECT INTO' devuelve más de una fila?",
            optionA = "TOO_MANY_ROWS",
            optionB = "NO_DATA_FOUND",
            optionC = "CURSOR_ALREADY_OPEN",
            optionD = "STORAGE_ERROR",
            correctOptionIndex = 0,
            explanation = "Para procesar múltiples filas se debe utilizar un cursor en lugar de un SELECT INTO escalar."
        ),

        // top_bd_4: Procedimientos y Funciones Almacenadas
        QuizQuestionEntity(
            id = "q_bd_4_1",
            topicId = "top_bd_4",
            theoryContext = "Una FUNCTION en PL/SQL está diseñada para calcular y retornar obligatoriamente un valor con la cláusula 'RETURN tipo_dato', y puede ser invocada directamente dentro de sentencias SQL (SELECT, WHERE). Un PROCEDURE ejecuta acciones y devuelve valores mediante parámetros OUT.",
            question = "¿Cuál es la diferencia fundamental entre una FUNCTION y un PROCEDURE en PL/SQL?",
            optionA = "Una FUNCTION debe retornar obligatoriamente un valor con RETURN y puede usarse en sentencias SQL; un PROCEDURE realiza acciones y retorna mediante parámetros OUT",
            optionB = "Un PROCEDURE no puede recibir parámetros",
            optionC = "Una FUNCTION solo puede ejecutarse en la consola de Linux",
            optionD = "Son exactamente lo mismo con diferente nombre",
            correctOptionIndex = 0,
            explanation = "Las funciones se usan para transformaciones y cálculos (ej. calcularIVA), mientras que los procedimientos manejan transacciones complejas."
        ),
        QuizQuestionEntity(
            id = "q_bd_4_2",
            topicId = "top_bd_4",
            theoryContext = "PL/SQL admite tres modos de parámetros: 'IN' (solo lectura de entrada), 'OUT' (solo escritura de salida hacia el invocador) y 'IN OUT' (recibe un valor inicial, lo procesa dentro del procedimiento y retorna el nuevo valor modificado).",
            question = "¿Qué modo de parámetro permite enviar un valor al procedimiento, modificarlo dentro y devolver el nuevo valor hacia quien lo llamó?",
            optionA = "IN OUT",
            optionB = "IN (modo por defecto, solo lectura)",
            optionC = "OUT (solo escritura de salida)",
            optionD = "RETURN ONLY",
            correctOptionIndex = 0,
            explanation = "'IN OUT' actúa como una variable bidireccional entre el programa principal y el subprograma."
        ),

        // top_bd_5: Paquetes y Triggers
        QuizQuestionEntity(
            id = "q_bd_5_1",
            topicId = "top_bd_5",
            theoryContext = "Un Paquete (Package) en Oracle se compone de dos partes obligatorias: la Especificación (Package Specification), que declara la interfaz pública de funciones y tipos; y el Cuerpo (Package Body), que contiene el código ejecutable y elementos privados.",
            question = "¿Cuáles son las dos partes que componen un Package (Paquete) en Oracle PL/SQL?",
            optionA = "Cabecera/Especificación (Package Specification) y Cuerpo (Package Body)",
            optionB = "Trigger y Procedure",
            optionC = "Esquema y Tabla",
            optionD = "Index y Constraint",
            correctOptionIndex = 0,
            explanation = "Los paquetes optimizan el rendimiento porque la primera llamada carga todo el paquete en la memoria compartida del servidor."
        ),
        QuizQuestionEntity(
            id = "q_bd_5_2",
            topicId = "top_bd_5",
            theoryContext = "En un Trigger a nivel de fila ('FOR EACH ROW'), Oracle provee los pseudorregistros ':NEW' y ':OLD'. ':NEW' contiene los nuevos datos que se están insertando o actualizando, mientras que ':OLD' contiene los valores existentes antes de la modificación.",
            question = "En un Trigger a nivel de fila (FOR EACH ROW) en Oracle, ¿qué calificador hace referencia al nuevo valor que se está insertando o actualizando?",
            optionA = ":NEW",
            optionB = ":OLD",
            optionC = ":CURRENT",
            optionD = ":NEXT",
            correctOptionIndex = 0,
            explanation = "':NEW' permite validar o modificar los datos entrantes (ej: :NEW.fecha_creacion := SYSDATE;) antes de guardarlos en la tabla."
        ),
        QuizQuestionEntity(
            id = "q_bd_5_3",
            topicId = "top_bd_5",
            theoryContext = "Los Triggers de base de datos son bloques PL/SQL que se disparan automáticamente en respuesta a eventos DML (INSERT, UPDATE, DELETE). Pueden ejecutarse 'BEFORE' (para validaciones o formateo previo) o 'AFTER' (para auditorías y registros de logs).",
            question = "¿En qué momentos o eventos DML puede dispararse automáticamente un Trigger en Oracle?",
            optionA = "BEFORE o AFTER de operaciones INSERT, UPDATE o DELETE",
            optionB = "Únicamente cuando se apaga la computadora",
            optionC = "Solo los fines de semana a medianoche",
            optionD = "Cuando se instala un programa en Windows",
            correctOptionIndex = 0,
            explanation = "Los triggers aseguran que las reglas de integridad se cumplan sin importar qué aplicación modifique la base de datos."
        ),

        // ══════════════════════════════════════════════════════════════════════════
        // DESARROLLO FULLSTACK II (DSY1104 - HTML5/CSS3/JS/REACT)
        // ══════════════════════════════════════════════════════════════════════════
        // top_fs_1: HTML5 Semántico
        QuizQuestionEntity(
            id = "q_fs_1_1",
            topicId = "top_fs_1",
            theoryContext = "El HTML5 Semántico utiliza etiquetas con significado estructural explícito (<header>, <nav>, <main>, <article>, <section>, <aside>, <footer>). Esto mejora la accesibilidad para lectores de pantalla, facilita la indexación de motores de búsqueda (SEO) y hace el código más legible que usar solo <div> genéricos.",
            question = "¿Por qué se prefieren etiquetas semánticas como <article>, <section>, <nav> y <header> en lugar de genéricos <div>?",
            optionA = "Mejoran la accesibilidad, el SEO y permiten a navegadores y motores de búsqueda comprender la jerarquía del contenido",
            optionB = "Porque aplican estilos CSS automáticamente sin necesidad de clases",
            optionC = "Son obligatorias para que el navegador ejecute JavaScript",
            optionD = "Aumentan la velocidad de descarga de imágenes",
            correctOptionIndex = 0,
            explanation = "El HTML semántico dota de significado y arquitectura a la información web."
        ),
        QuizQuestionEntity(
            id = "q_fs_1_2",
            topicId = "top_fs_1",
            theoryContext = "Para vincular una hoja de estilos externa (.css) a un documento HTML, se coloca la etiqueta '<link rel=\"stylesheet\" href=\"estilos.css\">' dentro de la sección '<head>', asegurando que los estilos se carguen antes de pintar los elementos en pantalla.",
            question = "¿Cómo se vincula una hoja de estilos CSS externa a un documento HTML?",
            optionA = "Mediante la etiqueta <link rel=\"stylesheet\" href=\"estilos.css\"> dentro de la sección <head>",
            optionB = "Con la etiqueta <style src=\"estilos.css\"> en el <body>",
            optionC = "Con la etiqueta <script href=\"estilos.css\"> en el <footer>",
            optionD = "Usando un iframe",
            correctOptionIndex = 0,
            explanation = "Ubicar el '<link>' en el '<head>' previene el parpadeo de contenido sin estilos (FOUC)."
        ),

        // top_fs_2: CSS3, Box Model, Flexbox y Grid
        QuizQuestionEntity(
            id = "q_fs_2_1",
            topicId = "top_fs_2",
            theoryContext = "En CSS Flexbox, 'justify-content' controla la alineación y distribución de los elementos a lo largo del eje principal (main axis, horizontal por defecto), mientras que 'align-items' controla la alineación a lo largo del eje transversal (cross axis, vertical por defecto).",
            question = "En CSS Flexbox, ¿qué propiedad alinea y distribuye los elementos hijos a lo largo del eje principal (main axis)?",
            optionA = "justify-content",
            optionB = "align-items",
            optionC = "align-content",
            optionD = "flex-direction",
            correctOptionIndex = 0,
            explanation = "'justify-content' admite valores como center, space-between, space-around y space-evenly."
        ),
        QuizQuestionEntity(
            id = "q_fs_2_2",
            topicId = "top_fs_2",
            theoryContext = "La función 'linear-gradient(direccion, color1, color2)' en CSS crea una transición progresiva y suave entre dos o más colores para usarla como fondo (background-image), logrando diseños modernos y atractivos sin tener que cargar imágenes pesadas.",
            question = "¿Qué es una función 'linear-gradient()' en CSS y cuál es su propósito visual?",
            optionA = "Crea una transición progresiva y suave entre dos o más colores para fondos sin requerir imágenes externas pesadas",
            optionB = "Calcula la velocidad de descarga de un video",
            optionC = "Aplica bordes redondeados a una tabla",
            optionD = "Centra el texto automáticamente en la pantalla",
            correctOptionIndex = 0,
            explanation = "Los degradados CSS se calculan por GPU, ofreciendo máxima nitidez y rendimiento en cualquier resolución de pantalla."
        ),
        QuizQuestionEntity(
            id = "q_fs_2_3",
            topicId = "top_fs_2",
            theoryContext = "El diseño web adaptable (Responsive Design) se logra configurando la etiqueta '<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">' en el <head> y usando Media Queries (@media (max-width: 768px)) para aplicar estilos según el tamaño de la pantalla.",
            question = "¿Cómo se asegura que una página web sea adaptable (responsive) a dispositivos móviles?",
            optionA = "Configurando el meta viewport en el <head> y utilizando Media Queries (@media) con unidades relativas (%, rem, flex, grid)",
            optionB = "Creando una página HTML distinta para cada modelo de celular",
            optionC = "Usando solo medidas fijas en píxeles (px)",
            optionD = "Desactivando el scroll vertical",
            correctOptionIndex = 0,
            explanation = "Las Media Queries permiten reorganizar menús, columnas y tipografías dinámicamente según el ancho de visualización."
        ),

        // top_fs_4: JavaScript Moderno y DOM
        QuizQuestionEntity(
            id = "q_fs_4_1",
            topicId = "top_fs_4",
            theoryContext = "La API Fetch de JavaScript realiza peticiones HTTP asíncronas basadas en Promesas. Al recibir la respuesta del servidor, el método 'response.json()' parsea el cuerpo de la respuesta convirtiéndolo en un objeto JavaScript iterable.",
            question = "¿Qué método de 'fetch()' en JavaScript se utiliza para procesar la respuesta en formato JSON de forma asíncrona?",
            optionA = "response.json()",
            optionB = "response.parse()",
            optionC = "JSON.decode(response)",
            optionD = "response.toObject()",
            correctOptionIndex = 0,
            explanation = "'response.json()' devuelve una Promesa que resuelve con el objeto JSON parseado."
        ),
        QuizQuestionEntity(
            id = "q_fs_4_2",
            topicId = "top_fs_4",
            theoryContext = "En JavaScript, el operador '===' (igualdad estricta) compara tanto el valor como el tipo de dato sin conversiones implícitas (ej: 5 === '5' es false). En contraste, '==' (igualdad débil) realiza coerción automática de tipos, lo que puede provocar comportamientos inesperados.",
            question = "¿Cuál es la diferencia entre '==' y '===' en JavaScript?",
            optionA = "'===' compara estrictamente valor Y tipo de dato, mientras que '==' compara solo valor haciendo coerción implícita de tipos",
            optionB = "'===' es para asignar variables y '==' para comparar",
            optionC = "'==' es más exacto que '==='",
            optionD = "Son idénticos desde ES6",
            correctOptionIndex = 0,
            explanation = "Se recomienda usar '===' por convención y buena práctica en todo el desarrollo moderno con JavaScript/TypeScript."
        ),
        QuizQuestionEntity(
            id = "q_fs_4_3",
            topicId = "top_fs_4",
            theoryContext = "Para crear interactividad en el menú hamburguesa móvil, JavaScript selecciona el botón con 'document.querySelector()' y añade un 'addEventListener(\"click\")' que alterna (toggle) una clase CSS (como '.active' o '.show') para abrir o cerrar la barra de navegación.",
            question = "¿Cuál es el rol de JavaScript en la interacción del menú hamburguesa en un sitio web responsivo?",
            optionA = "Escuchar el evento 'click' para alternar clases CSS (como '.active') que muestran u ocultan el menú en dispositivos móviles",
            optionB = "Descargar el código HTML de nuevo desde el servidor",
            optionC = "Comprimir los archivos CSS en tiempo real",
            optionD = "Reiniciar la sesión del usuario",
            correctOptionIndex = 0,
            explanation = "La manipulación del DOM mediante clases CSS separa la lógica interactiva de la presentación visual."
        ),

        // ══════════════════════════════════════════════════════════════════════════
        // ESTADÍSTICA DESCRIPTIVA (MAT4141 - PYTHON/PANDAS)
        // ══════════════════════════════════════════════════════════════════════════
        // top_est_1: Python y Pandas para Estadística
        QuizQuestionEntity(
            id = "q_est_1_1",
            topicId = "top_est_1",
            theoryContext = "En Pandas, el método 'df[\"columna\"].value_counts()' cuenta cuántas veces aparece cada categoría única en una Serie y devuelve el conteo ordenado de mayor a menor frecuencia absoluta (fi).",
            question = "¿Qué método de Pandas se utiliza en una columna categórica para obtener el conteo exacto de repeticiones (frecuencias absolutas) de cada categoría?",
            optionA = "df['columna'].value_counts()",
            optionB = "df['columna'].count_unique()",
            optionC = "df['columna'].sum_values()",
            optionD = "df['columna'].freq()",
            correctOptionIndex = 0,
            explanation = "'.value_counts()' es el método predilecto para construir tablas de frecuencia de variables cualitativas."
        ),
        QuizQuestionEntity(
            id = "q_est_1_2",
            topicId = "top_est_1",
            theoryContext = "El método 'df.describe()' en Pandas genera de forma instantánea una tabla resumen con los 8 estadísticos descriptivos más importantes de todas las columnas numéricas: count (conteo), mean (media), std (desviación estándar), min (mínimo), 25% (Q1), 50% (mediana/Q2), 75% (Q3) y max (máximo).",
            question = "¿Qué información devuelve por defecto el método 'df.describe()' sobre las columnas numéricas de un DataFrame?",
            optionA = "Conteo, media, desviación estándar, valor mínimo, percentiles 25% (Q1), 50% (mediana), 75% (Q3) y valor máximo",
            optionB = "Solo la suma total y el promedio",
            optionC = "Los nombres de las columnas y sus tipos de datos en memoria",
            optionD = "Una lista de valores nulos ordenados alfabéticamente",
            correctOptionIndex = 0,
            explanation = "'.describe()' permite un análisis exploratorio rápido (EDA) de las variables cuantitativas de un dataset."
        ),
        QuizQuestionEntity(
            id = "q_est_1_3",
            topicId = "top_est_1",
            theoryContext = "La función 'df.groupby(\"columna_agrupadora\")[\"columna_calculo\"].mean()' divide los datos en grupos basados en una variable categórica y calcula el promedio de cada grupo (ej: sueldo promedio por cada departamento de la empresa).",
            question = "Para calcular el promedio de sueldos agrupado por cada departamento en un DataFrame 'df', ¿cuál es la sintaxis correcta en Pandas?",
            optionA = "df.groupby('departamento')['sueldo'].mean()",
            optionB = "df.filter('departamento').average('sueldo')",
            optionC = "df.split('departamento').mean()",
            optionD = "df.aggregate('sueldo', by='departamento')",
            correctOptionIndex = 0,
            explanation = "'.groupby()' sigue el patrón 'Split-Apply-Combine' para realizar agregaciones estadísticas grupales."
        ),
        QuizQuestionEntity(
            id = "q_est_1_4",
            topicId = "top_est_1",
            theoryContext = "En Pandas, el filtrado de filas se realiza mediante indexación booleana: se coloca una condición lógica dentro de los corchetes del DataFrame (ej: df[df[\"edad\"] >= 18]), evaluando fila por fila y retornando solo las que dan True.",
            question = "¿Cómo se seleccionan en Pandas todas las filas donde la variable 'edad' sea mayor o igual a 18 en un DataFrame 'df'?",
            optionA = "df[df['edad'] >= 18]",
            optionB = "df.where('edad >= 18')",
            optionC = "df.filter(edad >= 18)",
            optionD = "df.select('edad >= 18')",
            correctOptionIndex = 0,
            explanation = "La indexación booleana permite combinar múltiples condiciones con operadores bit a bit como & (AND) y | (OR)."
        ),
        QuizQuestionEntity(
            id = "q_est_1_5",
            topicId = "top_est_1",
            theoryContext = "Para detectar valores faltantes (NaN / nulos) en un dataset con Pandas, se utiliza 'df.isna().sum()' (o 'df.isnull().sum()'), que genera una máscara booleana donde True representa un nulo y calcula el total de vacíos por columna.",
            question = "¿Qué instrucción permite conocer la cantidad de datos faltantes (nulos) por cada columna en un DataFrame 'df'?",
            optionA = "df.isna().sum()",
            optionB = "df.null_count()",
            optionC = "df.missing()",
            optionD = "df.empty_cells()",
            correctOptionIndex = 0,
            explanation = "Identificar datos faltantes es el primer paso obligatorio en la limpieza y preprocesamiento de datos."
        ),
        QuizQuestionEntity(
            id = "q_est_1_6",
            topicId = "top_est_1",
            theoryContext = "En Pandas, una 'Series' es un arreglo unidimensional etiquetado (1 columna con índice), mientras que un 'DataFrame' es una estructura bidimensional en formato de tabla (filas y columnas) compuesta por múltiples Series alineadas.",
            question = "¿Cuál es la diferencia estructural entre una Serie (Series) y un DataFrame en Pandas?",
            optionA = "Una Serie es una estructura unidimensional (1 sola columna con índice) y un DataFrame es bidimensional tabular (filas y columnas)",
            optionB = "La Serie almacena gráficos y el DataFrame números",
            optionC = "El DataFrame no tiene índices",
            optionD = "Una Serie solo admite números enteros",
            correctOptionIndex = 0,
            explanation = "Extraer una sola columna de un DataFrame ('df[\"nombre\"]') devuelve una 'Series'."
        ),

        // top_est_2: Clasificación de Variables y Tablas de Frecuencia
        QuizQuestionEntity(
            id = "q_est_2_1",
            topicId = "top_est_2",
            theoryContext = "Las variables cuantitativas expresan cantidades numéricas. Son 'discretas' cuando provienen de un conteo de números enteros aislados (ej: 0, 1, 2 asignaturas reprobadas) y 'continuas' cuando provienen de una medición que admite infinitos valores decimales en un intervalo (ej: 1.75 m de estatura).",
            question = "La variable 'Número de asignaturas reprobadas por un estudiante (0, 1, 2, 3...)' se clasifica como:",
            optionA = "Cuantitativa discreta",
            optionB = "Cuantitativa continua",
            optionC = "Cualitativa ordinal",
            optionD = "Cualitativa nominal",
            correctOptionIndex = 0,
            explanation = "No se pueden reprobar 1.5 asignaturas; al ser un conteo exacto de enteros, es cuantitativa discreta."
        ),
        QuizQuestionEntity(
            id = "q_est_2_2",
            topicId = "top_est_2",
            theoryContext = "Las variables cualitativas expresan cualidades o categorías. Son 'nominales' cuando no existe ningún orden o jerarquía entre las categorías (ej: Estado civil: Soltero, Casado, Viudo) y 'ordinales' cuando existe un orden natural evidente (ej: Nivel de satisfacción: Bajo, Medio, Alto).",
            question = "¿Cuál de las siguientes variables es un ejemplo de variable Cualitativa Nominal?",
            optionA = "Estado civil (Soltero, Casado, Viudo, Divorciado)",
            optionB = "Nivel de satisfacción en una encuesta (Bajo, Medio, Alto)",
            optionC = "Estatura de una persona en metros",
            optionD = "Puesto de llegada en una carrera (1°, 2°, 3° lugar)",
            correctOptionIndex = 0,
            explanation = "El estado civil no tiene un orden jerárquico inherente, por lo que es cualitativa nominal."
        ),
        QuizQuestionEntity(
            id = "q_est_2_3",
            topicId = "top_est_2",
            theoryContext = "En una tabla de frecuencias, la 'frecuencia relativa' (hi) es la proporción de observaciones de una categoría respecto al total de la muestra (N): hi = fi / N. Multiplicada por 100 da la frecuencia porcentual (hi%).",
            question = "Si en una muestra de 50 personas, 15 prefieren el turno mañana, ¿cuál es la frecuencia relativa (hi) y porcentual (hi%) de ese grupo?",
            optionA = "hi = 0.30 (30%)",
            optionB = "hi = 0.15 (15%)",
            optionC = "hi = 3.33 (33.3%)",
            optionD = "hi = 0.50 (50%)",
            correctOptionIndex = 0,
            explanation = "hi = 15 / 50 = 0.30. En porcentaje: 0.30 * 100 = 30%."
        ),
        QuizQuestionEntity(
            id = "q_est_2_4",
            topicId = "top_est_2",
            theoryContext = "La 'frecuencia absoluta acumulada' (Fi) es la suma sucesiva de las frecuencias absolutas (fi) desde la primera clase hasta la clase actual, indicando cuántas observaciones tienen un valor menor o igual al límite superior de ese intervalo.",
            question = "¿Qué representa la 'frecuencia absoluta acumulada' (Fi) de una clase en una tabla de frecuencias?",
            optionA = "La suma de las frecuencias absolutas desde la primera clase hasta la clase actual",
            optionB = "El porcentaje total de la muestra",
            optionC = "El promedio de los datos de esa clase",
            optionD = "El valor máximo registrado en la muestra",
            correctOptionIndex = 0,
            explanation = "El último valor de Fi en la tabla siempre coincide exactamente con el tamaño total de la muestra (N)."
        ),
        QuizQuestionEntity(
            id = "q_est_2_5",
            topicId = "top_est_2",
            theoryContext = "La 'Población' es el conjunto total y completo de todos los elementos o individuos sobre los que se desea obtener conclusiones. La 'Muestra' es un subconjunto representativo seleccionado de la población para realizar el estudio estadístico.",
            question = "¿Cuál es la diferencia fundamental entre una 'Población' y una 'Muestra'?",
            optionA = "La Población es la totalidad de elementos bajo estudio; la Muestra es un subconjunto representativo seleccionado de la población",
            optionB = "La Muestra siempre tiene más datos que la Población",
            optionC = "Población se usa solo en censos de personas y Muestra para animales",
            optionD = "Son sinónimos intercambiables en estadística",
            correctOptionIndex = 0,
            explanation = "Los valores calculados en la población se llaman 'Parámetros' (μ, σ) y los calculados en la muestra se llaman 'Estadísticos' (x̄, s)."
        ),

        // top_est_3: Medidas de Tendencia Central y Posición (Percentiles / Cuartiles)
        QuizQuestionEntity(
            id = "q_est_3_1",
            topicId = "top_est_3",
            theoryContext = "La Media Aritmética (promedio) es muy sensible a valores extremos o atípicos (outliers), los cuales distorsionan el resultado. La Mediana es una medida robusta porque depende de la posición central de los datos ordenados y no de la magnitud de los valores extremos.",
            question = "En una empresa donde 5 operarios ganan $500.000 y el gerente gana $15.000.000, ¿qué medida representa MEJOR el sueldo típico?",
            optionA = "La Mediana, porque es robusta e inmune al sesgo de valores extremos (outliers)",
            optionB = "La Media aritmética, porque utiliza todos los números en la suma",
            optionC = "La Varianza",
            optionD = "El Rango total",
            correctOptionIndex = 0,
            explanation = "La media ($2.916.666) no representa a la mayoría; la mediana ($500.000) refleja con exactitud el sueldo del trabajador representativo."
        ),
        QuizQuestionEntity(
            id = "q_est_3_2",
            topicId = "top_est_3",
            theoryContext = "Los Cuartiles dividen los datos ordenados en 4 partes iguales del 25% cada una. El Primer Cuartil (Q1) equivale al Percentil 25 (P25), el Segundo Cuartil (Q2) equivale al Percentil 50 (P50) y a la Mediana, y el Tercer Cuartil (Q3) equivale al Percentil 75 (P75).",
            question = "¿A qué percentil equivale exactamente el Segundo Cuartil (Q2) de un conjunto de datos?",
            optionA = "Al Percentil 50 (P50) y coincide con la Mediana",
            optionB = "Al Percentil 25 (P25)",
            optionC = "Al Percentil 75 (P75)",
            optionD = "Al Promedio Aritmético",
            correctOptionIndex = 0,
            explanation = "Q2 divide la distribución exactamente al 50%, coincidiendo siempre con la Mediana."
        ),
        QuizQuestionEntity(
            id = "q_est_3_3",
            topicId = "top_est_3",
            theoryContext = "El Rango Intercuartílico (IQR) se calcula restando el primer cuartil al tercer cuartil: IQR = Q3 - Q1. Representa la dispersión y amplitud que abarca el 50% central de los datos alrededor de la mediana.",
            question = "¿Cómo se calcula el Rango Intercuartílico (IQR) y qué porcentaje central de los datos abarca?",
            optionA = "IQR = Q3 - Q1, y contiene exactamente el 50% central de las observaciones",
            optionB = "IQR = Q3 + Q1, y contiene el 75% de los datos",
            optionC = "IQR = Max - Min, y contiene el 100% de los datos",
            optionD = "IQR = Media - Mediana, y contiene el 25% de los datos",
            correctOptionIndex = 0,
            explanation = "El IQR es la base para construir la caja en los diagramas Boxplot y detectar valores atípicos."
        ),
        QuizQuestionEntity(
            id = "q_est_3_4",
            topicId = "top_est_3",
            theoryContext = "Según la Regla de Tukey para diagramas de caja (Boxplot), los límites de los bigotes son: Límite Inferior = Q1 - 1.5 * IQR, y Límite Superior = Q3 + 1.5 * IQR. Cualquier dato que se encuentre fuera de estos límites se clasifica como valor atípico (outlier).",
            question = "En la regla de Tukey para diagramas de caja (Boxplot), ¿cuándo se considera un valor como atípico (outlier) superior?",
            optionA = "Cuando su valor es estrictamente mayor que Q3 + 1.5 * IQR",
            optionB = "Cuando supera a la Media + 1",
            optionC = "Cuando es mayor que el Percentil 50",
            optionD = "Cuando es el número más alto de la muestra sin importar su valor",
            correctOptionIndex = 0,
            explanation = "Los outliers se representan como puntos individuales aislados fuera de los bigotes en el gráfico."
        ),

        // top_est_4: Medidas de Dispersión y Homogeneidad
        QuizQuestionEntity(
            id = "q_est_4_1",
            topicId = "top_est_4",
            theoryContext = "La Varianza (s²) eleva las desviaciones al cuadrado, por lo que sus unidades de medida quedan al cuadrado (ej. $², kg²). La Desviación Estándar (s) es la raíz cuadrada de la varianza, lo que recupera la unidad de medida original lineal de los datos facilitando su interpretación.",
            question = "¿Por qué la Desviación Estándar (s) es más utilizada para interpretar resultados que la Varianza (s²)?",
            optionA = "Porque se expresa en las MISMAS unidades de medida originales de la variable (al ser la raíz cuadrada de la varianza)",
            optionB = "Porque siempre da un número negativo",
            optionC = "Porque no requiere ordenar los datos",
            optionD = "Porque elimina todos los valores duplicados",
            correctOptionIndex = 0,
            explanation = "Decir 'desviación de $50.000' es intuitivo y comparable; decir 'varianza de 2.500.000.000 $²' no es interpretable directamente."
        ),
        QuizQuestionEntity(
            id = "q_est_4_2",
            topicId = "top_est_4",
            theoryContext = "El Coeficiente de Variación (CV = (s / x̄) * 100%) es una medida de dispersión relativa adimensional. Permite comparar la variabilidad y homogeneidad entre dos conjuntos de datos con diferentes promedios o diferentes unidades de medida (menor CV = mayor homogeneidad / menor dispersión).",
            question = "¿Para qué se utiliza el Coeficiente de Variación (CV = (s / x̄) * 100%) en el análisis estadístico?",
            optionA = "Para comparar la variabilidad relativa y homogeneidad entre dos o más grupos con diferentes promedios o unidades de medida",
            optionB = "Para predecir el valor del próximo dato",
            optionC = "Para transformar datos cualitativos en cuantitativos",
            optionD = "Para calcular la probabilidad de una distribución normal",
            correctOptionIndex = 0,
            explanation = "Por criterio general, un CV <= 15-20% indica que el conjunto de datos es homogéneo y su media es altamente representativa."
        ),
        QuizQuestionEntity(
            id = "q_est_4_3",
            topicId = "top_est_4",
            theoryContext = "Al calcular la varianza de una muestra (s²), se divide por (n - 1) en lugar de n (corrección de Bessel). Esto compensa el hecho de que las desviaciones se calculan respecto a la media de la muestra (x̄) y no a la media poblacional (μ), obteniendo un estimador insesgado de σ².",
            question = "Al calcular la varianza de una muestra (s²), ¿por qué se divide por (n - 1) en lugar de n?",
            optionA = "Para aplicar la corrección de Bessel y obtener un estimador insesgado de la varianza poblacional (σ²)",
            optionB = "Porque se asume que un dato siempre es erróneo",
            optionC = "Para simplificar el cálculo manual",
            optionD = "Solo se aplica cuando la muestra tiene más de 100 datos",
            correctOptionIndex = 0,
            explanation = "Dividir por n - 1 corrige la subestimación natural que produce la varianza muestral."
        ),

        // top_est_5: Representación Gráfica e Interpretación
        QuizQuestionEntity(
            id = "q_est_5_1",
            topicId = "top_est_5",
            theoryContext = "El Histograma se utiliza para variables cuantitativas continuas agrupadas en intervalos de clase; sus barras están unidas entre sí sin espacio para reflejar la continuidad numérica. El Gráfico de Barras se usa para variables cualitativas o discretas y sus barras están separadas.",
            question = "¿Cuál es la diferencia fundamental entre un Histograma y un Gráfico de Barras?",
            optionA = "El Histograma representa variables cuantitativas continuas en intervalos contiguos (barras unidas); el Gráfico de Barras representa variables cualitativas o discretas (barras separadas)",
            optionB = "El Histograma solo usa sectores circulares",
            optionC = "El Gráfico de Barras es exclusivo para series de tiempo",
            optionD = "Son idénticos y solo cambia el color de las barras",
            correctOptionIndex = 0,
            explanation = "En el histograma, el área de cada rectángulo es proporcional a la frecuencia del intervalo de clase."
        ),
        QuizQuestionEntity(
            id = "q_est_5_2",
            topicId = "top_est_5",
            theoryContext = "En un Diagrama de Caja (Boxplot), la caja rectangular representa el 50% central de las observaciones, delimitada por el primer cuartil (Q1) y el tercer cuartil (Q3). La línea que divide el interior de la caja representa exactamente la Mediana (Q2).",
            question = "En un Diagrama de Caja (Boxplot), ¿qué estadístico representa la línea horizontal o vertical que corta la caja por dentro?",
            optionA = "La Mediana (Q2)",
            optionB = "La Media aritmética",
            optionC = "La Moda",
            optionD = "La Desviación Estándar",
            correctOptionIndex = 0,
            explanation = "Si la línea de la mediana está desplazada hacia uno de los extremos de la caja, indica asimetría en la distribución de los datos."
        ),
        QuizQuestionEntity(
            id = "q_est_5_3",
            topicId = "top_est_5",
            theoryContext = "Un Gráfico de Dispersión (Scatter Plot) grafica pares de observaciones (X, Y) en un plano cartesiano para evaluar visualmente si existe una relación o correlación entre dos variables cuantitativas (positiva, negativa, no lineal o nula).",
            question = "¿Para qué tipo de análisis es especialmente útil un Gráfico de Dispersión (Scatter Plot)?",
            optionA = "Para evaluar visualmente la relación, tendencia y correlación (lineal, no lineal o nula) entre dos variables cuantitativas",
            optionB = "Para mostrar porcentajes de una variable cualitativa",
            optionC = "Para ordenar datos alfabéticamente",
            optionD = "Para calcular la frecuencia acumulada",
            correctOptionIndex = 0,
            explanation = "Es el gráfico introductorio para el análisis de Regresión Lineal y cálculo del coeficiente de correlación de Pearson."
        ),

        // ══════════════════════════════════════════════════════════════════════════
        // ÉTICA PARA EL TRABAJO (EAY4730)
        // ══════════════════════════════════════════════════════════════════════════
        // top_et_1: Dimensión Moral en Informática
        QuizQuestionEntity(
            id = "q_et_1_1",
            topicId = "top_et_1",
            theoryContext = "La Responsabilidad Profesional en informática exige que los ingenieros y programadores actúen con diligencia, asegurando la calidad del software, la seguridad de los sistemas, la protección de datos personales y considerando el impacto social y humano de las soluciones que desarrollan.",
            question = "¿Qué establece el principio de 'Responsabilidad Profesional' en el desarrollo de software y sistemas?",
            optionA = "Velar por la seguridad, confidencialidad, fiabilidad del código y el bienestar de los usuarios afectados por el software",
            optionB = "Garantizar que el sistema funcione únicamente durante la entrega del proyecto",
            optionC = "Desentenderse de los fallos una vez firmado el contrato de entrega",
            optionD = "Cobrar tarifas superiores a la media del mercado",
            correctOptionIndex = 0,
            explanation = "El impacto de un fallo en software puede afectar vidas, finanzas o privacidad, por lo que la ética profesional es crítica."
        ),

        // top_et_2: Dilemas Éticos
        QuizQuestionEntity(
            id = "q_et_2_1",
            topicId = "top_et_2",
            theoryContext = "Un dilema ético ocurre cuando un profesional enfrenta exigencias o instrucciones que entran en conflicto con normativas legales, derechos de privacidad o códigos deontológicos (ej. recolección oculta de datos). La postura ética correcta es alertar formalmente sobre los riesgos y negarse a vulnerar la legalidad.",
            question = "Si un superior te pide implementar un mecanismo no documentado para recolectar datos de usuarios sin su consentimiento, ¿cuál es la conducta ética correcta?",
            optionA = "Advertir formalmente sobre la violación de privacidad, normativas legales (como GDPR o Ley de Protección de Datos) y negarse a vulnerar derechos",
            optionB = "Implementarlo en silencio para evitar conflictos laborales",
            optionC = "Vender los datos a un tercero para beneficio personal",
            optionD = "Renunciar sin explicar ningún motivo",
            correctOptionIndex = 0,
            explanation = "La obediencia jerárquica no exime al informático de su responsabilidad legal y ética ante la vulneración de derechos."
        ),

        // top_et_3: Privacidad y Códigos Deontológicos
        QuizQuestionEntity(
            id = "q_et_3_1",
            topicId = "top_et_3",
            theoryContext = "El Código de Ética y Conducta Profesional de ACM (Association for Computing Machinery) e IEEE Computer Society es el marco deontológico internacional más influyente en la ingeniería de software, estableciendo directrices sobre privacidad, honestidad, evitar daños y respeto a la propiedad intelectual.",
            question = "¿Qué organismo internacional publica el 'Código de Ética y Conducta Profesional' más reconocido en la industria informática?",
            optionA = "ACM (Association for Computing Machinery) e IEEE Computer Society",
            optionB = "La FIFA",
            optionC = "La Organización Mundial del Comercio",
            optionD = "El Consorcio W3C exclusivamente para HTML",
            correctOptionIndex = 0,
            explanation = "El código conjunto ACM/IEEE resume los principios que guían las decisiones técnicas con impacto ético en la sociedad."
        )
    )
}
