package com.example.semfour.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Entidad Room para preguntas de evaluación y repaso interactivo (Active Recall / Quiz).
 * Cada tema tiene entre 2 y 3 preguntas precargadas y el usuario puede añadir más.
 */
@Serializable
@Entity(
    tableName = "quiz_questions",
    foreignKeys = [
        ForeignKey(
            entity = TopicEntity::class,
            parentColumns = ["id"],
            childColumns = ["topicId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["topicId"])
    ]
)
data class QuizQuestionEntity(
    @PrimaryKey val id: String,
    val topicId: String,
    /** Concepto explicativo o lección que enseña la teoría antes de responder */
    val theoryContext: String = "",
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    /** Índice de la opción correcta: 0 = A, 1 = B, 2 = C, 3 = D */
    val correctOptionIndex: Int,
    /** Explicación conceptual que se revela tras contestar */
    val explanation: String = "",
    /** true si fue creada manualmente por el estudiante */
    val isCustom: Boolean = false,
    /** Métricas de aciertos del usuario */
    val timesAnswered: Int = 0,
    val timesCorrect: Int = 0,
    val lastAnsweredAt: Long? = null
)
