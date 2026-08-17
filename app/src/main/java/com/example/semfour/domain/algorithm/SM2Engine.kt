package com.example.semfour.domain.algorithm

import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Motor SM-2 Adaptado para SemFour.
 *
 * Implementación del algoritmo SuperMemo 2 (SM-2) de Piotr Woźniak con adaptaciones
 * para integrar el nivel de confianza subjetivo del estudiante.
 *
 * ## Fórmulas originales SM-2:
 *
 * Factor de Facilidad (EF):
 *   EF' = EF + (0.1 - (5 - q) * (0.08 + (5 - q) * 0.02))
 *   Donde q ∈ [0, 5] es la calificación del repaso.
 *   Restricción: EF' >= 1.3 (EF nunca baja de 1.3)
 *
 * Intervalo:
 *   n = 1: I(1) = 1 día
 *   n = 2: I(2) = 6 días
 *   n > 2: I(n) = round(I(n-1) × EF')
 *
 * Si q < 3 (respuesta fallida): resetear n=0, I=1, EF permanece.
 *
 * ## Adaptación SemFour:
 * El [nivelConfianza] (1-5) actúa como un modificador inicial de EF:
 *   EF_inicial = 1.3 + (nivelConfianza - 1) * 0.3
 *   (nivel 1 → EF=1.3, nivel 5 → EF=2.5+)
 *
 * Esto hace que temas con bajo nivel de confianza aparezcan más frecuentemente
 * desde el inicio, antes de que el algoritmo puro tome el control.
 */
object SM2Engine {

    /** EF mínimo absoluto permitido por SM-2 */
    const val EF_MIN = 1.3
    /** EF por defecto para temas nuevos con nivel de confianza = 3 */
    const val EF_DEFAULT = 2.5

    /**
     * Calcula el EF inicial basado en el nivel de confianza subjetivo del estudiante.
     *
     * @param nivelConfianza Nivel 1-5 asignado al tema en el seed data
     * @return Factor de facilidad inicial para este tema
     */
    fun efFromNivelConfianza(nivelConfianza: Int): Double {
        // nivel 1 → 1.3, nivel 2 → 1.6, nivel 3 → 1.9, nivel 4 → 2.2, nivel 5 → 2.5
        return EF_MIN + (nivelConfianza.coerceIn(1, 5) - 1) * 0.3
    }

    /**
     * Procesa una respuesta de repaso y devuelve el estado SM-2 actualizado.
     *
     * @param q Calificación del repaso (0-5):
     *   - 0: Blackout total
     *   - 1: Incorrecto, respuesta familiar al verla
     *   - 2: Incorrecto, fácil de recordar
     *   - 3: Correcto con dificultad importante
     *   - 4: Correcto con pequeña duda
     *   - 5: Respuesta perfecta
     * @param currentEF Factor de facilidad actual del tema
     * @param currentInterval Intervalo actual en días
     * @param currentRepetitions Número de repasos exitosos consecutivos
     * @param ahora Timestamp actual en ms (por defecto: System.currentTimeMillis())
     * @return [SM2Result] con el nuevo estado y timestamp del próximo repaso
     */
    fun procesarRespuesta(
        q: Int,
        currentEF: Double,
        currentInterval: Int,
        currentRepetitions: Int,
        ahora: Long = System.currentTimeMillis()
    ): SM2Result {
        require(q in 0..5) { "La calificación q debe estar entre 0 y 5, recibido: $q" }

        return if (q < 3) {
            // ── Respuesta fallida: reiniciar repeticiones e intervalo ──────────
            // EF no cambia en respuesta fallida (diseño SM-2 original)
            val nuevoEF = recalcularEF(currentEF, q) // sí puede bajar el EF
            SM2Result(
                nuevoEF = nuevoEF,
                nuevoIntervalo = 1,
                nuevasRepeticiones = 0,
                ultimoRepaso = ahora,
                proximoRepaso = ahora + diasAMs(1)
            )
        } else {
            // ── Respuesta exitosa: calcular nuevo intervalo ────────────────────
            val nuevoEF = recalcularEF(currentEF, q)
            val nuevasReps = currentRepetitions + 1
            val nuevoIntervalo = when (nuevasReps) {
                1 -> 1
                2 -> 6
                else -> (currentInterval * nuevoEF).roundToInt().coerceAtLeast(1)
            }
            SM2Result(
                nuevoEF = nuevoEF,
                nuevoIntervalo = nuevoIntervalo,
                nuevasRepeticiones = nuevasReps,
                ultimoRepaso = ahora,
                proximoRepaso = ahora + diasAMs(nuevoIntervalo)
            )
        }
    }

    /**
     * Recalcula el Factor de Facilidad (EF) tras una respuesta.
     *
     * Fórmula: EF' = EF + 0.1 - (5-q)(0.08 + (5-q) * 0.02)
     * Simplificada: EF' = EF - 0.8 + 0.28q - 0.02q²
     */
    private fun recalcularEF(ef: Double, q: Int): Double {
        val delta = 0.1 - (5 - q) * (0.08 + (5 - q) * 0.02)
        return max(EF_MIN, ef + delta)
    }

    /** Convierte días a milisegundos */
    private fun diasAMs(dias: Int): Long = dias.toLong() * 24 * 60 * 60 * 1000L
}

/**
 * Resultado del procesamiento SM-2 para un repaso.
 *
 * @property nuevoEF Factor de facilidad actualizado
 * @property nuevoIntervalo Nuevo intervalo en días
 * @property nuevasRepeticiones Contador de repasos exitosos consecutivos
 * @property ultimoRepaso Timestamp del repaso actual (ms)
 * @property proximoRepaso Timestamp calculado del próximo repaso (ms)
 */
data class SM2Result(
    val nuevoEF: Double,
    val nuevoIntervalo: Int,
    val nuevasRepeticiones: Int,
    val ultimoRepaso: Long,
    val proximoRepaso: Long
)
