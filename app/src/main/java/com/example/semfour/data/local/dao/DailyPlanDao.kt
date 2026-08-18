package com.example.semfour.data.local.dao

import androidx.room.*
import com.example.semfour.data.local.entity.DailyPlanTaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyPlanDao {

    @Query("SELECT * FROM daily_plan_tasks ORDER BY weekNumber ASC, dayOfWeek ASC, id ASC")
    fun getAllTasks(): Flow<List<DailyPlanTaskEntity>>

    @Query("SELECT * FROM daily_plan_tasks WHERE weekNumber = :weekNumber ORDER BY dayOfWeek ASC, id ASC")
    fun getTasksForWeek(weekNumber: Int): Flow<List<DailyPlanTaskEntity>>

    @Query("SELECT * FROM daily_plan_tasks WHERE weekNumber = :weekNumber AND dayOfWeek = :dayOfWeek ORDER BY id ASC")
    fun getTasksForDay(weekNumber: Int, dayOfWeek: Int): Flow<List<DailyPlanTaskEntity>>

    @Query("SELECT COUNT(*) FROM daily_plan_tasks")
    suspend fun getTaskCount(): Int

    @Query("SELECT COUNT(*) FROM daily_plan_tasks WHERE isCompleted = 1")
    fun getCompletedTaskCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM daily_plan_tasks")
    fun getTotalTaskCountFlow(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTasks(tasks: List<DailyPlanTaskEntity>)

    @Query("UPDATE daily_plan_tasks SET isCompleted = :isCompleted, completedAt = :completedAt WHERE id = :taskId")
    suspend fun updateTaskStatus(taskId: String, isCompleted: Boolean, completedAt: Long?)

    @Query("DELETE FROM daily_plan_tasks")
    suspend fun clearAll()
}
