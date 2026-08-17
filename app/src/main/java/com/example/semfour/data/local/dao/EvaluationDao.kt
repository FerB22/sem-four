package com.example.semfour.data.local.dao

import androidx.room.*
import com.example.semfour.data.local.entity.EvaluationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EvaluationDao {

    @Query("SELECT * FROM evaluations WHERE completada = 0 ORDER BY CASE WHEN fechaEval <= 0 THEN 1 ELSE 0 END ASC, fechaEval ASC")
    fun getUpcomingEvaluations(): Flow<List<EvaluationEntity>>

    @Query("SELECT * FROM evaluations WHERE subjectId = :subjectId ORDER BY CASE WHEN fechaEval <= 0 THEN 1 ELSE 0 END ASC, fechaEval ASC")
    fun getEvaluationsForSubject(subjectId: String): Flow<List<EvaluationEntity>>

    /** Evaluaciones próximas en los siguientes [dias] días */
    @Query("SELECT * FROM evaluations WHERE completada = 0 AND fechaEval BETWEEN :ahora AND :hasta ORDER BY fechaEval ASC")
    suspend fun getEvaluationsUntil(ahora: Long, hasta: Long): List<EvaluationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvaluation(evaluation: EvaluationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvaluations(evaluations: List<EvaluationEntity>)

    @Update
    suspend fun updateEvaluation(evaluation: EvaluationEntity)

    @Query("UPDATE evaluations SET completada = 1, updatedAt = :ts WHERE id = :id")
    suspend fun markCompleted(id: String, ts: Long = System.currentTimeMillis())

    @Query("UPDATE evaluations SET fechaEval = :fechaEval, updatedAt = :ts WHERE id = :id")
    suspend fun updateEvaluationDate(id: String, fechaEval: Long, ts: Long = System.currentTimeMillis())

    @Delete
    suspend fun deleteEvaluation(evaluation: EvaluationEntity)

    @Query("SELECT COUNT(*) FROM evaluations")
    suspend fun count(): Int

    @Query("DELETE FROM evaluations WHERE id IN (:ids)")
    suspend fun deleteEvaluationsByIds(ids: List<String>)
}
