package com.example.semfour.data.local.dao

import androidx.room.*
import com.example.semfour.data.local.entity.StudySessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudySessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: StudySessionEntity)

    @Query("SELECT * FROM study_sessions WHERE topicId = :topicId ORDER BY completedAt DESC")
    fun getSessionsForTopic(topicId: String): Flow<List<StudySessionEntity>>

    @Query("SELECT * FROM study_sessions WHERE subjectId = :subjectId ORDER BY completedAt DESC LIMIT :limit")
    fun getRecentSessionsForSubject(subjectId: String, limit: Int = 20): Flow<List<StudySessionEntity>>

    /** Sesiones completadas hoy (para el dashboard) */
    @Query("SELECT * FROM study_sessions WHERE completedAt >= :inicioDia ORDER BY completedAt DESC")
    fun getTodaySessions(inicioDia: Long): Flow<List<StudySessionEntity>>

    /** Total de minutos estudiados hoy */
    @Query("SELECT COALESCE(SUM(durationMinutes), 0) FROM study_sessions WHERE completedAt >= :inicioDia")
    fun getTodayMinutes(inicioDia: Long): Flow<Int>

    /** Total de sesiones de la semana */
    @Query("SELECT COUNT(*) FROM study_sessions WHERE completedAt >= :inicioSemana")
    fun getWeekSessionCount(inicioSemana: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM study_sessions WHERE completedAt >= :inicioDia")
    suspend fun getTodaySessionCount(inicioDia: Long): Int
}
