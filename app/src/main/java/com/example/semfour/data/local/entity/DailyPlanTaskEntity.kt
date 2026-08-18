package com.example.semfour.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa una tarea operativa diaria dentro del Cronograma de Estudio de 16 semanas.
 */
@Entity(tableName = "daily_plan_tasks")
data class DailyPlanTaskEntity(
    @PrimaryKey
    val id: String,                  // Ej: "plan_w01_d1_bd"
    val weekNumber: Int,             // 1..16
    val dayOfWeek: Int,              // 1=Lunes, 2=Martes, 3=Miércoles, 4=Jueves, 5=Viernes
    val subjectId: String,           // "sub_bd", "sub_poo", "sub_etica", "sub_fullstack", "sub_estadistica", "sub_movil", "sub_consolidacion"
    val taskType: String,            // "Completar cuaderno", "Teoría y Ejercicios Nivel 1", etc.
    val notebookFile: String,        // "Semana_01_Bloques_Anonimos_RECORD_PLSQL.ipynb"
    val isCompleted: Boolean = false,
    val completedAt: Long? = null
)
