package com.example.semfour.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.semfour.data.local.dao.*
import com.example.semfour.data.local.entity.*

/**
 * Base de datos Room de SemFour — Fuente de verdad local (Offline-First).
 */
@Database(
    entities = [
        SubjectEntity::class,
        TopicEntity::class,
        StudySessionEntity::class,
        EvaluationEntity::class,
        HabitDayEntity::class,
        ScheduleEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun topicDao(): TopicDao
    abstract fun studySessionDao(): StudySessionDao
    abstract fun evaluationDao(): EvaluationDao
    abstract fun habitDayDao(): HabitDayDao
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        const val DATABASE_NAME = "semfour_db"
    }
}
