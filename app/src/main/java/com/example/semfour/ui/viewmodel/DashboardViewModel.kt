package com.example.semfour.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semfour.data.local.entity.EvaluationEntity
import com.example.semfour.data.local.entity.HabitDayEntity
import com.example.semfour.data.local.entity.ScheduleEntity
import com.example.semfour.data.local.entity.SubjectEntity
import com.example.semfour.data.repository.StudyRepository
import com.example.semfour.domain.algorithm.PrioritizedTopic
import com.example.semfour.widget.WidgetDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/**
 * ViewModel del Dashboard y Pantallas de Seguimiento.
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val studyRepository: StudyRepository,
    private val widgetDataManager: WidgetDataManager
) : ViewModel() {

    val prioritizedTopics: StateFlow<List<PrioritizedTopic>> = studyRepository
        .getPrioritizedTopics()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val subjects: StateFlow<List<SubjectEntity>> = studyRepository
        .getAllSubjects()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSchedule: StateFlow<List<ScheduleEntity>> = studyRepository
        .getAllSchedule()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentDayOfWeek: Int = getDayOfWeekIndex()

    val todaySchedule: StateFlow<List<ScheduleEntity>> = studyRepository
        .getScheduleForDay(currentDayOfWeek)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todayMinutes: StateFlow<Int> = studyRepository
        .getTodayMinutes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val last90DaysHabits: StateFlow<List<HabitDayEntity>> = studyRepository
        .getLast90DaysHabits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val weeklyMinutes: StateFlow<Int> = studyRepository
        .getWeeklyMinutes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val upcomingEvaluations: StateFlow<List<EvaluationEntity>> = studyRepository
        .getUpcomingEvaluations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** El tema con mayor prioridad para mostrar en la tarjeta principal */
    val topPriorityTopic: StateFlow<PrioritizedTopic?> = prioritizedTopics
        .map { it.firstOrNull() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /** Temas que DEBEN repasarse hoy (vencidos según SM-2) */
    val dueTodayCount: StateFlow<Int> = prioritizedTopics
        .map { topics -> topics.count { it.estaVencido || it.esNuevo } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun updateEvaluationDate(evalId: String, fechaEval: Long) {
        viewModelScope.launch {
            studyRepository.updateEvaluationDate(evalId, fechaEval)
        }
    }

    fun saveEvaluation(evaluation: EvaluationEntity) {
        viewModelScope.launch {
            studyRepository.insertEvaluation(evaluation)
        }
    }

    fun deleteEvaluation(evaluation: EvaluationEntity) {
        viewModelScope.launch {
            studyRepository.deleteEvaluation(evaluation)
        }
    }

    init {
        // Actualizar widgets reactivamente en hilo secundario (IO) para no bloquear la UI
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            combine(
                topPriorityTopic,
                subjects,
                todaySchedule
            ) { topTopic, subjectList, classesToday ->
                Triple(topTopic, subjectList, classesToday)
            }.collectLatest { (topTopic, subjectList, classesToday) ->
                val subjectName = subjectList.find { it.id == topTopic?.topic?.subjectId }?.nombre
                    ?: "SemFour"
                val nextClass = classesToday.firstOrNull()
                val nextClassSubject = subjectList.find { it.id == nextClass?.subjectId }?.nombre ?: ""
                val nextClassText = if (nextClass != null) "${nextClass.startTime} $nextClassSubject" else ""
                val nextClassRoom = nextClass?.room ?: ""

                widgetDataManager.updatePriorityWidget(
                    prioritizedTopic = topTopic,
                    subjectName = subjectName,
                    nextClass = nextClassText,
                    nextClassRoom = nextClassRoom
                )
            }
        }

        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            combine(
                last90DaysHabits,
                todayMinutes,
                weeklyMinutes
            ) { habits, todayMins, weekMins ->
                val currentStreak = habits.firstOrNull()?.streakDay ?: 0
                widgetDataManager.updateStreakWidget(
                    streakDays = currentStreak,
                    todayMinutes = todayMins,
                    weeklyMinutes = weekMins
                )
            }.collect()
        }

        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            allSchedule.collectLatest {
                widgetDataManager.updateWeeklyScheduleWidget()
            }
        }
    }

    companion object {
        fun getDayOfWeekIndex(): Int {
            val cal = Calendar.getInstance()
            return when (cal.get(Calendar.DAY_OF_WEEK)) {
                Calendar.MONDAY -> 1
                Calendar.TUESDAY -> 2
                Calendar.WEDNESDAY -> 3
                Calendar.THURSDAY -> 4
                Calendar.FRIDAY -> 5
                Calendar.SATURDAY -> 6
                else -> 7
            }
        }
    }
}
