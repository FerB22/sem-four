package com.example.semfour.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Entidad Room para un tema de estudio dentro de una asignatura.
 *
 * Campos del algoritmo SM-2:
 * - [nivelConfianza]: Calificación subjetiva 1-5 (inicializada con datos del seed)
 * - [factorFacilidad]: EF (Ease Factor), inicia en 2.5, mínimo 1.3
 * - [intervaloDias]: Días hasta el próximo repaso (n=1→1d, n=2→6d, n>2→I*EF)
 * - [ultimoRepaso]: Timestamp del último repaso completado
 * - [proximoRepaso]: Timestamp calculado del próximo repaso (= ultimoRepaso + intervaloDias)
 * - [repeticiones]: Contador de repasos exitosos consecutivos (q >= 3)
 *
 * Campos adicionales:
 * - [tiempoEstudiadoAcumulado]: Minutos totales estudiados en este tema
 * - [driveLinksJson]: JSON array de URLs de Google Drive adjuntos (apuntes, PDFs)
 */
@Serializable
@Entity(
    tableName = "topics",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["subjectId"]),
        Index(value = ["proximoRepaso"]),
        Index(value = ["nivelConfianza"])
    ]
)
data class TopicEntity(
    @PrimaryKey val id: String,
    val subjectId: String,
    val nombre: String,

    // ── SM-2 campos ──────────────────────────────────────
    /** Nivel de confianza del estudiante: 1 (no sé nada) → 5 (dominio total) */
    val nivelConfianza: Int = 3,
    /** Factor de facilidad SM-2 (EF). Rango [1.3, 5.0], inicio: 2.5 */
    val factorFacilidad: Double = 2.5,
    /** Intervalo actual en días hasta el próximo repaso */
    val intervaloDias: Int = 1,
    /** Número de repasos exitosos consecutivos (q >= 3) */
    val repeticiones: Int = 0,
    /** Timestamp (ms) del último repaso. Null si nunca se ha repasado */
    val ultimoRepaso: Long? = null,
    /** Timestamp (ms) del próximo repaso recomendado (calculado por SM-2) */
    val proximoRepaso: Long = System.currentTimeMillis(),

    // ── Métricas de estudio ───────────────────────────────
    /** Minutos totales acumulados estudiando este tema */
    val tiempoEstudiadoAcumulado: Long = 0L,

    // ── Links de Google Drive ─────────────────────────────
    /** JSON Array de DriveLink (serializado). Ej: [{"url":"...","nombre":"Apunte 1"}] */
    val driveLinksJson: String = "[]",

    val updatedAt: Long = System.currentTimeMillis()
)
