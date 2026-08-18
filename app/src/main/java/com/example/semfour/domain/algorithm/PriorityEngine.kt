package com.example.semfour.domain.algorithm

import com.example.semfour.data.local.entity.EvaluationEntity
import com.example.semfour.data.local.entity.TopicEntity
import kotlin.math.max

/**
 * Motor de priorización diaria de temas de estudio.
 *
 * ## Fórmula de Score de Prioridad:
 *
 *   Score = w1 * score_sm2 + w2 * score_confianza + w3 * score_urgencia
 *
 * Pesos:
 *   w1 = 0.50  → Repetición espaciada (SM-2): temas vencidos o por vencer
 *   w2 = 0.30  → Nivel de confianza inverso: menor confianza = mayor prioridad
 *   w3 = 0.20  → Urgencia: proximidad a evaluaciones calendarizadas
 *
 * ### Componente SM-2 (score_sm2):
 *   días_vencido = max(0, (ahora - proximoRepaso) / 86_400_000)
 *   score_sm2 = tanh(días_vencido / 7.0)  → se satura en ~1.0 a los 20+ días
 *   (temas nunca repasados obtienen score_sm2 = 1.0)
 *
 * ### Componente Confianza (score_confianza):
 *   score_confianza = (6 - nivelConfianza) / 5.0
 *   (nivel 1 → 1.0, nivel 3 → 0.6, nivel 5 → 0.2)
 *
 * ### Componente Urgencia (score_urgencia):
 *   Para cada evaluación de la asignatura del tema:
 *     dias_restantes = (fechaEval - ahora) / 86_400_000
 *     urgencia = max(0, 1 - dias_restantes / 14.0)  → 100% urgencia si < 0 días
 *   score_urgencia = max de todas las urgencias de la asignatura
 */
object PriorityEngine {

    private const val W_SM2 = 0.50
    private const val W_CONFIANZA = 0.30
    private const val W_URGENCIA = 0.20
    private const val MS_POR_DIA = 86_400_000L
    private const val VENTANA_URGENCIA_DIAS = 14.0

    /**
     * Calcula la lista de temas ordenada por prioridad descendente.
     *
     * @param topics Lista de todos los temas activos
     * @param evaluaciones Lista de evaluaciones próximas (todas las asignaturas)
     * @param ahora Timestamp actual en ms
     * @return Lista de [PrioritizedTopic] ordenada: mayor score primero
     */
    fun calcularPrioridades(
        topics: List<TopicEntity>,
        evaluaciones: List<EvaluationEntity>,
        ahora: Long = System.currentTimeMillis()
    ): List<PrioritizedTopic> {
        // Pre-calcular urgencia máxima por asignatura
        val urgenciaPorAsignatura = calcularUrgenciasPorAsignatura(evaluaciones, ahora)

        return topics.map { topic ->
            val scoreSM2 = calcularScoreSM2(topic, ahora)
            val scoreConfianza = calcularScoreConfianza(topic.nivelConfianza)
            val scoreUrgencia = urgenciaPorAsignatura[topic.subjectId] ?: 0.0

            val scoreFinal = (W_SM2 * scoreSM2 + W_CONFIANZA * scoreConfianza + W_URGENCIA * scoreUrgencia).coerceIn(0.0, 1.0)

            PrioritizedTopic(
                topic = topic,
                score = scoreFinal,
                scoreSM2 = scoreSM2,
                scoreConfianza = scoreConfianza,
                scoreUrgencia = scoreUrgencia,
                diasVencido = calcularDiasVencido(topic, ahora)
            )
        }.sortedByDescending { it.score }
    }

    /**
     * Score SM-2: cuánto tiempo lleva vencido el tema.
     * Función tanh para suavizar la saturación (no-lineal).
     * Temas nuevos (nunca repasados) obtienen score máximo = 1.0.
     */
    private fun calcularScoreSM2(topic: TopicEntity, ahora: Long): Double {
        if (topic.ultimoRepaso == null) return 1.0  // Nunca estudiado → máxima prioridad SM-2

        val diasVencido = max(0.0, (ahora - topic.proximoRepaso).toDouble() / MS_POR_DIA)
        // tanh(x/7): llega a 0.9 a los ~14 días, se satura progresivamente
        return Math.tanh(diasVencido / 7.0).coerceIn(0.0, 1.0)
    }

    /** Score de confianza inverso: menor confianza = mayor prioridad */
    private fun calcularScoreConfianza(nivelConfianza: Int): Double {
        return ((6 - nivelConfianza.coerceIn(1, 5)) / 5.0).coerceIn(0.0, 1.0)
    }

    /** Calcula días que lleva vencido un tema (negativo si aún no vence) */
    private fun calcularDiasVencido(topic: TopicEntity, ahora: Long): Double {
        return (ahora - topic.proximoRepaso).toDouble() / MS_POR_DIA
    }

    /**
     * Pre-calcula la urgencia máxima para cada asignatura basándose en
     * sus evaluaciones próximas no completadas con fecha definida.
     */
    private fun calcularUrgenciasPorAsignatura(
        evaluaciones: List<EvaluationEntity>,
        ahora: Long
    ): Map<String, Double> {
        return evaluaciones
            .filter { !it.completada && it.fechaEval > 0L }
            .groupBy { it.subjectId }
            .mapValues { (_, evals) ->
                evals.maxOfOrNull { eval ->
                    val diasRestantes = (eval.fechaEval - ahora).toDouble() / MS_POR_DIA
                    // Urgencia = 1.0 si es hoy o ya pasó, 0.0 si faltan 14+ días
                    val urgencia = (1.0 - (diasRestantes / VENTANA_URGENCIA_DIAS)).coerceIn(0.0, 1.0)
                    urgencia * eval.ponderacion
                } ?: 0.0
            }
    }
}

/**
 * Tema priorizado con su score calculado y los componentes individuales.
 * Útil para UI (mostrar razón de la prioridad) y debugging.
 *
 * @property topic Entidad del tema
 * @property score Score final ponderado [0.0, 1.0]
 * @property scoreSM2 Contribución del componente SM-2
 * @property scoreConfianza Contribución del componente de confianza
 * @property scoreUrgencia Contribución del componente de urgencia
 * @property diasVencido Días desde que venció (negativo = aún no vence)
 */
data class PrioritizedTopic(
    val topic: TopicEntity,
    val score: Double,
    val scoreSM2: Double,
    val scoreConfianza: Double,
    val scoreUrgencia: Double,
    val diasVencido: Double
) {
    /** True si el tema está vencido (debería haberse repasado hoy o antes) */
    val estaVencido: Boolean get() = diasVencido > 0
    /** True si el tema nunca ha sido repasado */
    val esNuevo: Boolean get() = topic.ultimoRepaso == null
}
