package com.example.semfour.data.local.dao

import androidx.room.*
import com.example.semfour.data.local.entity.TopicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {

    @Query("SELECT * FROM topics WHERE subjectId = :subjectId ORDER BY nivelConfianza ASC, proximoRepaso ASC")
    fun getTopicsBySubject(subjectId: String): Flow<List<TopicEntity>>

    @Query("SELECT * FROM topics ORDER BY nivelConfianza ASC, proximoRepaso ASC")
    fun getAllTopics(): Flow<List<TopicEntity>>

    @Query("SELECT * FROM topics WHERE id = :id")
    suspend fun getTopicById(id: String): TopicEntity?

    /** Temas cuyo próximo repaso ya venció (para SM-2 activo) */
    @Query("SELECT * FROM topics WHERE proximoRepaso <= :ahora ORDER BY proximoRepaso ASC")
    suspend fun getOverdueTopics(ahora: Long = System.currentTimeMillis()): List<TopicEntity>

    /** Temas que vencen en los próximos [diasHastaFin] días */
    @Query("SELECT * FROM topics WHERE proximoRepaso BETWEEN :ahora AND :hasta ORDER BY proximoRepaso ASC")
    suspend fun getTopicsUpcomingInDays(ahora: Long, hasta: Long): List<TopicEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopic(topic: TopicEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopics(topics: List<TopicEntity>)

    @Update
    suspend fun updateTopic(topic: TopicEntity)

    @Query("UPDATE topics SET tiempoEstudiadoAcumulado = tiempoEstudiadoAcumulado + :minutos, updatedAt = :ts WHERE id = :topicId")
    suspend fun addStudyTime(topicId: String, minutos: Long, ts: Long = System.currentTimeMillis())

    @Query("UPDATE topics SET nivelConfianza = :nivel, updatedAt = :ts WHERE id = :topicId")
    suspend fun updateNivelConfianza(topicId: String, nivel: Int, ts: Long = System.currentTimeMillis())

    @Query("SELECT COUNT(*) FROM topics")
    suspend fun count(): Int

    @Query("DELETE FROM topics WHERE id IN (:ids)")
    suspend fun deleteTopicsByIds(ids: List<String>)

    @Query("SELECT SUM(tiempoEstudiadoAcumulado) FROM topics WHERE subjectId = :subjectId")
    fun getTotalTimeForSubject(subjectId: String): Flow<Long?>
}
