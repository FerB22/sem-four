package com.example.semfour.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Entidad Room para una sesión de estudio completada.
 *
 * Registra cada bloque de tiempo de estudio, ya sea:
 * - [POMODORO]: Sesión estándar de 25 minutos
 * - [MICRO]: Micro-sesión anti-fricción de 5-10 minutos
 * - [FREE]: Cronómetro libre sin tiempo predefinido
 *
 * La [calificacionSM2] es la retroalimentación del estudiante al finalizar
 * la sesión (0-5), que alimenta directamente al motor SM-2:
 * - 0: No recuerdo nada (blackout)
 * - 1: Respuesta incorrecta, pero al ver la respuesta era familiar
 * - 2: Respuesta incorrecta, pero fácil de recordar
 * - 3: Respuesta correcta con dificultad significativa
 * - 4: Respuesta correcta con pequeña duda
 * - 5: Respuesta perfecta sin duda
 */
@Serializable
@Entity(
    tableName = "study_sessions",
    foreignKeys = [
        ForeignKey(
            entity = TopicEntity::class,
            parentColumns = ["id"],
            childColumns = ["topicId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["topicId"]),
        Index(value = ["subjectId"]),
        Index(value = ["completedAt"])
    ]
)
data class StudySessionEntity(
    @PrimaryKey val id: String,
    val topicId: String,
    val subjectId: String,
    val durationMinutes: Int,
    /** Calificación SM-2 del estudiante al finalizar: 0 (fallo total) → 5 (perfecto) */
    val calificacionSM2: Int,
    /** Tipo de sesión: POMODORO | MICRO | FREE */
    val sessionType: String,
    /** Timestamp de cuándo completó la sesión */
    val completedAt: Long = System.currentTimeMillis()
)
