package com.example.semfour.data.local.dao

import androidx.room.*
import com.example.semfour.data.local.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedule ORDER BY dayOfWeek ASC, startTime ASC")
    fun getAllSchedule(): Flow<List<ScheduleEntity>>

    @Query("SELECT * FROM schedule WHERE dayOfWeek = :dayOfWeek ORDER BY startTime ASC")
    fun getScheduleForDay(dayOfWeek: Int): Flow<List<ScheduleEntity>>

    @Query("SELECT * FROM schedule WHERE dayOfWeek = :dayOfWeek ORDER BY startTime ASC")
    suspend fun getScheduleForDayDirect(dayOfWeek: Int): List<ScheduleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(items: List<ScheduleEntity>)

    @Query("SELECT COUNT(*) FROM schedule")
    suspend fun count(): Int

    @Query("DELETE FROM schedule WHERE subjectId = :subjectId")
    suspend fun deleteScheduleForSubject(subjectId: String)

    @Query("DELETE FROM schedule")
    suspend fun clear()
}
