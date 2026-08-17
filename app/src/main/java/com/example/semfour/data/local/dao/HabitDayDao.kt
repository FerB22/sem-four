package com.example.semfour.data.local.dao

import androidx.room.*
import com.example.semfour.data.local.entity.HabitDayEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDayDao {

    @Query("SELECT * FROM habit_days WHERE dateKey >= :desde ORDER BY dateKey ASC")
    fun getHabitDaysSince(desde: String): Flow<List<HabitDayEntity>>

    /** Últimos 90 días para el heatmap estilo GitHub */
    @Query("SELECT * FROM habit_days ORDER BY dateKey DESC LIMIT 90")
    fun getLast90Days(): Flow<List<HabitDayEntity>>

    @Query("SELECT * FROM habit_days WHERE dateKey = :dateKey")
    suspend fun getHabitDay(dateKey: String): HabitDayEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHabitDay(habitDay: HabitDayEntity)

    /** Racha actual: número de días consecutivos hasta hoy con al menos 1 sesión */
    @Query("""
        SELECT COALESCE(MAX(streakDay), 0) 
        FROM habit_days 
        WHERE dateKey <= :hoy AND sessionsCount > 0
    """)
    suspend fun getCurrentStreak(hoy: String): Int

    /** Minutos totales de la semana */
    @Query("SELECT COALESCE(SUM(totalMinutes), 0) FROM habit_days WHERE dateKey >= :inicioSemana")
    fun getWeeklyMinutes(inicioSemana: String): Flow<Int>

    @Query("SELECT COALESCE(SUM(totalMinutes), 0) FROM habit_days")
    fun getTotalMinutesAllTime(): Flow<Long>
}
