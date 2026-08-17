package com.example.semfour.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Entidad Room para certámenes, entregas y evaluaciones del semestre.
 * Alimenta el componente de "urgencia" del algoritmo de priorización.
 */
@Serializable
@Entity(
    tableName = "evaluations",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["subjectId"]), Index(value = ["fechaEval"])]
)
data class EvaluationEntity(
    @PrimaryKey val id: String,
    val subjectId: String,
    val nombre: String,
    /** Timestamp de la fecha de la evaluación */
    val fechaEval: Long,
    /** Peso porcentual en la nota final (0.0 a 1.0). Ej: 0.30 = 30% */
    val ponderacion: Float,
    /** Tipo: CERTAMEN | ENTREGA | QUIZ | PROYECTO */
    val tipo: String,
    val completada: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)
