package com.example.semfour.domain.algorithm

import com.example.semfour.data.local.entity.EvaluationEntity
import com.example.semfour.data.local.entity.TopicEntity
import org.junit.Assert.*
import org.junit.Test

class SM2EngineTest {

    @Test
    fun `efFromNivelConfianza scales correctly from level 1 to 5`() {
        assertEquals(1.3, SM2Engine.efFromNivelConfianza(1), 0.001)
        assertEquals(1.6, SM2Engine.efFromNivelConfianza(2), 0.001)
        assertEquals(1.9, SM2Engine.efFromNivelConfianza(3), 0.001)
        assertEquals(2.2, SM2Engine.efFromNivelConfianza(4), 0.001)
        assertEquals(2.5, SM2Engine.efFromNivelConfianza(5), 0.001)
    }

    @Test
    fun `failed review resets repetitions and interval to 1`() {
        val now = 1000000L
        val result = SM2Engine.procesarRespuesta(
            q = 1,
            currentEF = 2.5,
            currentInterval = 10,
            currentRepetitions = 3,
            ahora = now
        )

        assertEquals(1, result.nuevoIntervalo)
        assertEquals(0, result.nuevasRepeticiones)
        assertEquals(now + 86400000L, result.proximoRepaso)
        assertTrue(result.nuevoEF >= SM2Engine.EF_MIN)
    }

    @Test
    fun `first successful review sets interval to 1 and increments repetitions`() {
        val now = 1000000L
        val result = SM2Engine.procesarRespuesta(
            q = 4,
            currentEF = 2.5,
            currentInterval = 1,
            currentRepetitions = 0,
            ahora = now
        )

        assertEquals(1, result.nuevoIntervalo)
        assertEquals(1, result.nuevasRepeticiones)
    }

    @Test
    fun `second successful review sets interval to 6`() {
        val now = 1000000L
        val result = SM2Engine.procesarRespuesta(
            q = 5,
            currentEF = 2.5,
            currentInterval = 1,
            currentRepetitions = 1,
            ahora = now
        )

        assertEquals(6, result.nuevoIntervalo)
        assertEquals(2, result.nuevasRepeticiones)
    }

    @Test
    fun `third successful review multiplies previous interval by new EF`() {
        val now = 1000000L
        val result = SM2Engine.procesarRespuesta(
            q = 5,
            currentEF = 2.5,
            currentInterval = 6,
            currentRepetitions = 2,
            ahora = now
        )

        // q=5: delta = 0.1, newEF = 2.6. interval = round(6 * 2.6) = 16
        assertEquals(3, result.nuevasRepeticiones)
        assertEquals(16, result.nuevoIntervalo)
    }

    @Test
    fun `PriorityEngine prioritizes low confidence topics and overdue topics`() {
        val now = 2000000000L
        val topic1 = TopicEntity(
            id = "t1",
            subjectId = "s1",
            nombre = "Corrutinas",
            nivelConfianza = 1,
            factorFacilidad = 1.3,
            intervaloDias = 1,
            ultimoRepaso = null,
            proximoRepaso = now - 86400000L
        )

        val topic2 = TopicEntity(
            id = "t2",
            subjectId = "s2",
            nombre = "HTML Semantico",
            nivelConfianza = 4,
            factorFacilidad = 2.5,
            intervaloDias = 6,
            ultimoRepaso = now - 86400000L,
            proximoRepaso = now + 500000000L
        )

        val evals = listOf(
            EvaluationEntity(
                id = "e1",
                subjectId = "s1",
                nombre = "Certamen 1",
                fechaEval = now + 86400000L * 2, // en 2 días (muy urgente)
                ponderacion = 0.3f,
                tipo = "CERTAMEN",
                completada = false
            )
        )

        val prioritized = PriorityEngine.calcularPrioridades(listOf(topic1, topic2), evals, ahora = now)

        assertEquals("t1", prioritized.first().topic.id)
        assertTrue(prioritized.first().score > prioritized.last().score)
    }
}
