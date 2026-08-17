package com.example.semfour.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Entidad Room para registrar la actividad de estudio por día.
 * Alimenta el heatmap estilo GitHub y el contador de racha.
 *
 * La clave primaria [dateKey] usa formato "YYYY-MM-DD" para facilitar
 * consultas por rango de fechas sin conversiones de timestamp.
 */
@Serializable
@Entity(tableName = "habit_days")
data class HabitDayEntity(
    /** Fecha en formato "YYYY-MM-DD". Ej: "2026-08-15" */
    @PrimaryKey val dateKey: String,
    /** Minutos totales de estudio en este día */
    val totalMinutes: Int = 0,
    /** Número de sesiones completadas en este día */
    val sessionsCount: Int = 0,
    /** Número del día en la racha consecutiva. 0 si se rompió la racha */
    val streakDay: Int = 0
)
