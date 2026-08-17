package com.example.semfour.data.local.entity

import androidx.room.Entity
import kotlinx.serialization.Serializable
import androidx.room.PrimaryKey

/**
 * Entidad Room para una asignatura del semestre.
 * Representa el nivel "Subject" en la jerarquía Subject → Topic → Session.
 */
@Serializable
@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    val codigo: String,
    /** Nombre del icono de Material Icons (ej: "smartphone", "code", "web") */
    val icono: String,
    /** Color hex de la asignatura (ej: "#3DDC84") */
    val color: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
