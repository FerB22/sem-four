package com.example.semfour.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Entidad Room para las sesiones de clases del horario semanal.
 */
@Serializable
@Entity(
    tableName = "schedule",
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
        Index(value = ["dayOfWeek", "startTime"])
    ]
)
data class ScheduleEntity(
    @PrimaryKey val id: String,
    val subjectId: String,
    /** 1=Lunes, 2=Martes, 3=Miércoles, 4=Jueves, 5=Viernes, 6=Sábado */
    val dayOfWeek: Int,
    val startTime: String,
    val endTime: String,
    val room: String,
    val professor: String
)
