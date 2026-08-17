package com.example.semfour.data.repository

import com.example.semfour.data.local.DatabaseSeeder
import com.example.semfour.data.local.dao.*
import com.example.semfour.data.local.entity.*
import com.example.semfour.domain.algorithm.PriorityEngine
import com.example.semfour.domain.algorithm.PrioritizedTopic
import com.example.semfour.domain.algorithm.SM2Engine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositorio central de estudio — fuente de verdad offline-first.
 */
@Singleton
class StudyRepository @Inject constructor(
    private val subjectDao: SubjectDao,
    private val topicDao: TopicDao,
    private val sessionDao: StudySessionDao,
    private val evaluationDao: EvaluationDao,
    private val habitDayDao: HabitDayDao,
    private val scheduleDao: ScheduleDao,
    private val databaseSeeder: DatabaseSeeder
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // ── Subjects ──────────────────────────────────────────────────────────────

    fun getAllSubjects(): Flow<List<SubjectEntity>> = subjectDao.getAllSubjects()

    suspend fun getSubjectById(id: String): SubjectEntity? = subjectDao.getSubjectById(id)

    // ── Schedule (Horario Oficial) ────────────────────────────────────────────

    fun getAllSchedule(): Flow<List<ScheduleEntity>> = scheduleDao.getAllSchedule()

    fun getScheduleForDay(dayOfWeek: Int): Flow<List<ScheduleEntity>> =
        scheduleDao.getScheduleForDay(dayOfWeek)

    suspend fun getScheduleForDayDirect(dayOfWeek: Int): List<ScheduleEntity> =
        scheduleDao.getScheduleForDayDirect(dayOfWeek)

    // ── Topics ────────────────────────────────────────────────────────────────

    fun getTopicsForSubject(subjectId: String): Flow<List<TopicEntity>> =
        topicDao.getTopicsBySubject(subjectId)

    fun getAllTopics(): Flow<List<TopicEntity>> = topicDao.getAllTopics()

    suspend fun getTopicById(id: String): TopicEntity? = topicDao.getTopicById(id)

    suspend fun updateNivelConfianza(topicId: String, nivel: Int) {
        topicDao.updateNivelConfianza(topicId, nivel)
    }

    // ── Experiencias de Aprendizaje y Asignaturas Opcionales ─────────────────

    suspend fun isExperienceEnabled(expNumber: Int): Boolean =
        databaseSeeder.isExperienceEnabled(expNumber)

    suspend fun setExperienceEnabled(expNumber: Int, enabled: Boolean) {
        databaseSeeder.setExperienceEnabled(expNumber, enabled)
    }

    suspend fun isEnglishEnabled(): Boolean =
        databaseSeeder.isEnglishEnabled()

    suspend fun setEnglishEnabled(enabled: Boolean) {
        databaseSeeder.setEnglishEnabled(enabled)
    }

    // ── SM-2 + Sesión de Estudio ──────────────────────────────────────────────

    suspend fun registrarSesion(
        topicId: String,
        subjectId: String,
        durationMinutes: Int,
        calificacionSM2: Int,
        sessionType: String
    ) {
        val topic = topicDao.getTopicById(topicId) ?: return
        val ahora = System.currentTimeMillis()
        val sessionId = UUID.randomUUID().toString()

        sessionDao.insertSession(
            StudySessionEntity(
                id = sessionId,
                topicId = topicId,
                subjectId = subjectId,
                durationMinutes = durationMinutes,
                calificacionSM2 = calificacionSM2,
                sessionType = sessionType,
                completedAt = ahora
            )
        )

        val sm2Result = SM2Engine.procesarRespuesta(
            q = calificacionSM2,
            currentEF = topic.factorFacilidad,
            currentInterval = topic.intervaloDias,
            currentRepetitions = topic.repeticiones,
            ahora = ahora
        )

        val nuevoNivelConfianza = when {
            calificacionSM2 >= 4 -> minOf(5, topic.nivelConfianza + 1)
            calificacionSM2 <= 1 -> maxOf(1, topic.nivelConfianza - 1)
            else -> topic.nivelConfianza
        }
        topicDao.updateTopic(
            topic.copy(
                nivelConfianza = nuevoNivelConfianza,
                factorFacilidad = sm2Result.nuevoEF,
                intervaloDias = sm2Result.nuevoIntervalo,
                repeticiones = sm2Result.nuevasRepeticiones,
                ultimoRepaso = sm2Result.ultimoRepaso,
                proximoRepaso = sm2Result.proximoRepaso,
                tiempoEstudiadoAcumulado = topic.tiempoEstudiadoAcumulado + durationMinutes,
                updatedAt = ahora
            )
        )

        actualizarHabitDelDia(durationMinutes, ahora)
    }

    // ── Prioridades ────────────────────────────────────────────────────────────

    fun getPrioritizedTopics(): Flow<List<PrioritizedTopic>> =
        combine(
            topicDao.getAllTopics(),
            evaluationDao.getUpcomingEvaluations()
        ) { topics, evaluaciones ->
            PriorityEngine.calcularPrioridades(topics, evaluaciones)
        }

    // ── Evaluaciones ──────────────────────────────────────────────────────────

    fun getUpcomingEvaluations(): Flow<List<EvaluationEntity>> =
        evaluationDao.getUpcomingEvaluations()

    suspend fun insertEvaluation(evaluation: EvaluationEntity) =
        evaluationDao.insertEvaluation(evaluation)

    suspend fun markEvaluationCompleted(id: String) =
        evaluationDao.markCompleted(id)

    suspend fun updateEvaluationDate(id: String, fechaEval: Long) =
        evaluationDao.updateEvaluationDate(id, fechaEval)

    suspend fun deleteEvaluation(evaluation: EvaluationEntity) =
        evaluationDao.deleteEvaluation(evaluation)

    // ── Sesiones ──────────────────────────────────────────────────────────────

    fun getTodaySessions(): Flow<List<StudySessionEntity>> {
        val inicioDia = getInicioDiaMs()
        return sessionDao.getTodaySessions(inicioDia)
    }

    fun getTodayMinutes(): Flow<Int> {
        val inicioDia = getInicioDiaMs()
        return sessionDao.getTodayMinutes(inicioDia)
    }

    // ── Hábitos ───────────────────────────────────────────────────────────────

    fun getLast90DaysHabits(): Flow<List<HabitDayEntity>> = habitDayDao.getLast90Days()

    fun getWeeklyMinutes(): Flow<Int> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        val inicioSemana = dateFormat.format(cal.time)
        return habitDayDao.getWeeklyMinutes(inicioSemana)
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private suspend fun actualizarHabitDelDia(minutosNuevos: Int, ahora: Long) {
        val dateKey = dateFormat.format(Date(ahora))
        val existente = habitDayDao.getHabitDay(dateKey)

        val ayerStr = dateFormat.format(Date(ahora - 86_400_000L))
        val streakAyer = habitDayDao.getCurrentStreak(ayerStr)
        val streakHoy = if (existente != null && existente.sessionsCount > 0) {
            existente.streakDay
        } else {
            streakAyer + 1
        }

        habitDayDao.upsertHabitDay(
            HabitDayEntity(
                dateKey = dateKey,
                totalMinutes = (existente?.totalMinutes ?: 0) + minutosNuevos,
                sessionsCount = (existente?.sessionsCount ?: 0) + 1,
                streakDay = streakHoy
            )
        )
    }

    private fun getInicioDiaMs(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}
